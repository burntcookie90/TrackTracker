package me.vishnu.tracktracker.shared.stores.welcome

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.vishnu.tracktracker.shared.stores.Store

class WelcomeStore : Store<WelcomeModel, WelcomeEvents, WelcomeEffects>,
  CoroutineScope by CoroutineScope(Dispatchers.Main) {

  private val state = MutableStateFlow(WelcomeModel())
  private val sideEffect = MutableSharedFlow<WelcomeEffects>()

  override fun start(init: () -> Set<WelcomeEffects>) {
    launch {
      init().forEach {
        Napier.d("Handling init: $it", tag = "Meow")
        sideEffect.emit(it)
      }
    }
  }

  override fun observeState() = state
  override fun observeSideEffect() = sideEffect

  override fun dispatch(event: WelcomeEvents) {
    val oldState = state.value
    when (event) {
      WelcomeEvents.AddCar -> launch { state.value = oldState.copy(addCarMode = true) }
      WelcomeEvents.DismissAddCarDialog -> launch {
        state.value = oldState.copy(addCarMode = false)
      }
      is WelcomeEvents.CreateCar -> launch {
        sideEffect.emit(WelcomeEffects.CreateCar(event.car))
        state.value = oldState.copy(addCarMode = false)
      }
      is WelcomeEvents.InitialDataLoaded -> launch {
        state.value = oldState.copy(cars = event.cars)
      }
    }
  }
}