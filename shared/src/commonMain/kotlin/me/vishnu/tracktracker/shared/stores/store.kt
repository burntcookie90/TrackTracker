package me.vishnu.tracktracker.shared.stores

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface Model
interface Event
interface Effect

interface Store<M : Model, E : Event, F : Effect> {
  fun start(
    init: () -> Set<F> = { emptySet() },
    eventSources: List<Flow<E>> = emptyList(),
    logTag: String? = null
  )

  fun observeState(): StateFlow<M>
  fun observeSideEffect(): Flow<F>
  fun dispatch(event: E)
}

sealed class Next<M : Model, F : Effect> {
  data class WithModel<M : Model, F : Effect>(val model: M) : Next<M, F>()
  data class WithModelAndEffects<M : Model, F : Effect>(
    val model: M,
    val effects: Set<F>
  ) : Next<M, F>()

  data class WithEffects<M : Model, F : Effect>(val effects: Set<F>) : Next<M, F>()

  class NoChange<M : Model, F : Effect> : Next<M, F>()
}

abstract class ActualStore<M : Model, E : Event, F : Effect>(
  initialModel: M,
) : Store<M, E, F>, CoroutineScope by CoroutineScope(Dispatchers.Main) {
  private val state: MutableStateFlow<M> = MutableStateFlow(initialModel)
  private val sideEffects: MutableSharedFlow<F> = MutableSharedFlow(extraBufferCapacity = 1)
  private var logTag: String? = null
  private val nextStream = MutableSharedFlow<Next<M, F>>()

  override fun start(
    init: () -> Set<F>,
    eventSources: List<Flow<E>>,
    logTag: String?
  ) {
    this.logTag = logTag
    launch {
      init().forEach {
        logTag?.let { tag -> Napier.d("Running init effect: $it", tag = tag) }
        sideEffects.emit(it)
      }
    }

    launch { eventSources.forEach { it.collect(::dispatch) } }

    launch {
      nextStream.collect { next ->
        logTag?.let { Napier.d("Next $next", tag = it) }
        when (next) {
          is Next.NoChange<M, F> -> {}
          is Next.WithEffects<M, F> -> next.effects.forEach { sideEffects.tryEmit(it) }
          is Next.WithModel<M, F> -> state.emit(next.model)
          is Next.WithModelAndEffects<M, F> -> {
            state.emit(next.model)
            next.effects.forEach { sideEffects.emit(it) }
          }
        }
      }
    }
  }

  override fun observeState() = state
  override fun observeSideEffect() = sideEffects
  override fun dispatch(event: E) {
    logTag?.let { Napier.d("Event received: $event", tag = it) }
    launch {
      nextStream.emit(update(state.value, event))
    }
  }

  abstract fun update(model: M, event: E): Next<M, F>

  fun noChange() = Next.NoChange<M, F>()
  fun next(model: M) = Next.WithModel<M, F>(model = model)
  fun next(vararg effects: F) = Next.WithEffects<M, F>(effects = effects.toSet())

  fun next(model: M, vararg effects: F) = Next.WithModelAndEffects(
    model = model,
    effects = effects.toSet()
  )
}

abstract class EffectHandler<F : Effect, E : Event> :
  CoroutineScope by CoroutineScope(Dispatchers.Default) {
  abstract val handler: suspend (value: F) -> Flow<E?>
  lateinit var dispatch: (E) -> Unit
    private set

  fun bindToStore(effectFlow: Flow<F>, dispatch: (E) -> Unit) {
    this.dispatch = dispatch
    launch {
      effectFlow.flatMapConcat(handler)
        .filterNotNull()
        .collect { dispatch(it) }
    }
  }

  protected fun consume(effectHandler: () -> Unit): Flow<E?> {
    effectHandler()
    return flowOf(null)
  }
}

class Loop<M : Model, E : Event, F : Effect, H : EffectHandler<F, E>>(
  private val store: Store<M, E, F>,
  effectHandler: H,
  startEffects: () -> Set<F> = { emptySet() },
  eventSources: List<Flow<E>> = emptyList(),
  logTag: String? = null
) {

  @Suppress("MemberVisibilityCanBePrivate")
  val state: StateFlow<M>

  @Suppress("MemberVisibilityCanBePrivate")
  val eventCallback: (E) -> Unit

  init {
    effectHandler.bindToStore(store.observeSideEffect(), store::dispatch)
    store.start(startEffects, eventSources, logTag)

    state = store.observeState()
    eventCallback = store::dispatch
  }

  operator fun component1() = state
  operator fun component2() = eventCallback
}
