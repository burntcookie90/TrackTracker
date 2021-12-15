package me.vishnu.tracktracker.shared.stores

import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.log

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

abstract class ActualStore<M : Model, E : Event, F : Effect>(
  initialModel: M,
) : Store<M, E, F>, CoroutineScope by CoroutineScope(Dispatchers.Main) {
  private val state: MutableStateFlow<M> = MutableStateFlow(initialModel)
  private val sideEffects: MutableSharedFlow<F> = MutableSharedFlow(extraBufferCapacity = 1)
  private var logTag: String? = null

  override fun start(
    init: () -> Set<F>,
    eventSources: List<Flow<E>>,
    logTag: String?
  ) {
    this.logTag = logTag
    launch {
      init().forEach {
        logTag?.let { tag ->
          Napier.d("Running init effect: $it", tag = tag)
        }
        sideEffects.emit(it)
      }
    }

    launch {
      eventSources.forEach { it.collect(::dispatch) }
    }
  }

  override fun observeState() = state
  override fun observeSideEffect() = sideEffects
  override fun dispatch(event: E) {
    logTag?.let {
      Napier.d("Event received: $event", tag = it)
    }
    update(state.value, event)
  }

  abstract fun update(model: M, event: E)

  fun next(model: M, vararg effects: F) {
    launch {
      logTag?.let {
        Napier.d("Next: model: $model", tag = it)
      }
      state.emit(model)
      dispatchEffects(*effects)
    }
  }

  fun dispatchEffects(vararg effects: F) {
    logTag?.let {
      Napier.d("Disptching effects: $effects", tag = it)
    }
    launch {
      effects.forEach {
        sideEffects.emit(it)
      }
    }
  }
}

abstract class EffectHandler<F : Effect, E : Event> :
  CoroutineScope by CoroutineScope(Dispatchers.Unconfined) {
  abstract val handler: suspend (value: F) -> E?
  lateinit var dispatch: (E) -> Unit
    private set

  fun bindToStore(effectFlow: Flow<F>, dispatch: (E) -> Unit) {
    this.dispatch = dispatch
    launch {
      effectFlow.map(handler).collect {
        if (it != null) {
          dispatch(it)
        }
      }
    }
  }

  protected fun consume(effectHandler: () -> Unit): E? {
    effectHandler()
    return null
  }
}

class Loop<M : Model, E : Event, F : Effect, H : EffectHandler<F, E>>(
  private val store: Store<M, E, F>,
  effectHandler: H,
  startEffects: () -> Set<F> = { emptySet() },
  eventSources: List<Flow<E>> = emptyList(),
  logTag: String? = null
) {

  val state: StateFlow<M>
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
