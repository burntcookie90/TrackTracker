package me.vishnu.tracktracker.shared.stores

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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

abstract class EffectHandler<F : Effect, E : Event> :
  CoroutineScope by CoroutineScope(Dispatchers.Main) {
  abstract val handler: suspend (value: F) -> Unit
  lateinit var eventDispatch: (E) -> Unit
    private set

  fun bindToStore(effectFlow: Flow<F>, eventDispatch: (E) -> Unit) {
    Napier.d("Effect Handler bind", tag = "Meow")
    this.eventDispatch = eventDispatch
    launch {
      effectFlow.collect(handler)
    }
  }
}

fun <M : Model, E : Event, F : Effect> Loop(
  store: Store<M, E, F>,
  effectHandler: EffectHandler<F, E>,
  init: () -> Set<F>,
): Pair<StateFlow<M>, (E) -> Unit> {
  effectHandler.bindToStore(store.observeSideEffect(), store::dispatch)
  store.start(init)
  return store.observeState() to store::dispatch
}