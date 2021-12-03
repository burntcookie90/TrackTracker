package me.vishnu.tracktracker.shared.stores

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface Model
interface Event
interface Effect

interface Store<M : Model, E : Event, F : Effect> {
  fun start(init: () -> Set<F> = { emptySet() })
  fun observeState(): StateFlow<M>
  fun observeSideEffect(): Flow<F>
  fun dispatch(event: E)
}

abstract class ActualStore<M : Model, E : Event, F : Effect>(
  initialModel: M,
) : Store<M, E, F>, CoroutineScope by CoroutineScope(Dispatchers.Main) {
  private val state: MutableStateFlow<M> = MutableStateFlow(initialModel)
  private val sideEffects: MutableSharedFlow<F> = MutableSharedFlow()

  override fun start(init: () -> Set<F>) {
    launch {
      init().forEach { sideEffects.emit(it) }
    }
  }

  override fun observeState() = state
  override fun observeSideEffect() = sideEffects
  override fun dispatch(event: E) = update(state.value, event)

  abstract fun update(model: M, event: E)

  fun next(model: M, vararg effects: F) {
    launch {
      state.value = model
      dispatch(*effects)
    }
  }

  fun dispatch(vararg effects: F) {
    launch {
      effects.forEach { sideEffects.emit(it) }
    }
  }
}

abstract class EffectHandler<F : Effect, E : Event> :
  CoroutineScope by CoroutineScope(Dispatchers.Main) {
  abstract val handler: suspend (value: F) -> Unit
  lateinit var dispatch: (E) -> Unit
    private set

  fun bindToStore(effectFlow: Flow<F>, dispatch: (E) -> Unit) {
    this.dispatch = dispatch
    launch {
      effectFlow.collect(handler)
    }
  }
}

class Loop<M : Model, E : Event, F : Effect, H : EffectHandler<F, E>>(
  private val store: Store<M, E, F>,
  effectHandler: H,
  init: () -> Set<F>,
) {

  val state: StateFlow<M>
  val eventCallback: (E) -> Unit

  init {
    effectHandler.bindToStore(store.observeSideEffect(), store::dispatch)
    store.start(init)

    state = store.observeState()
    eventCallback = store::dispatch
  }

  operator fun component1() = state
  operator fun component2() = eventCallback
}
