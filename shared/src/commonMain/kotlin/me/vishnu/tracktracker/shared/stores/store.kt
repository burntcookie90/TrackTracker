package me.vishnu.tracktracker.shared.stores

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.vishnu.tracktracker.shared.stores.welcome.WelcomeEffects
import me.vishnu.tracktracker.shared.stores.welcome.WelcomeModel

interface Model
interface Event
interface Effect

interface Store<M : Model, E : Event, F : Effect> {
  fun start(init: () -> Set<F> = { emptySet() })
  fun observeState(): StateFlow<M>
  fun observeSideEffect(): Flow<F>
  fun dispatch(event: E)
}

abstract class EffectHandler<F : Effect, E : Event> :
  CoroutineScope by CoroutineScope(Dispatchers.Main) {
  abstract val handler: suspend (value: F) -> Unit
  lateinit var dispatch: (E) -> Unit
    private set

  fun bindToStore(effectFlow: Flow<F>, dispatch: (E) -> Unit) {
    Napier.d("Effect Handler bind", tag = "Meow")
    this.dispatch = dispatch
    launch {
      effectFlow.collect(handler)
    }
  }
}

fun <M : Model, E : Event, F : Effect, H: EffectHandler<F, E>> loop(
  store: Store<M, E, F>,
  effectHandler: H,
  init: () -> Set<F>,
): Pair<StateFlow<M>, (E) -> Unit> {
  effectHandler.bindToStore(store.observeSideEffect(), store::dispatch)
  store.start(init)
  return store.observeState() to store::dispatch
}

class Loop<M : Model, E : Event, F : Effect, H: EffectHandler<F, E>>(
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
