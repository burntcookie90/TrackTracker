package me.vishnu.tracktracker.shared.stores.welcome

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.stores.Store

@Inject
class CarScreenStore : Store<CarScreenModel, CarScreenEvents, CarScreenEffects>,
  CoroutineScope by CoroutineScope(Dispatchers.Main) {

  private val state = MutableStateFlow(CarScreenModel())
  private val sideEffect = MutableSharedFlow<CarScreenEffects>()

  override fun start(init: () -> Set<CarScreenEffects>) {
    launch {
      init().forEach {
        sideEffect.emit(it)
      }
    }
  }

  override fun observeState() = state
  override fun observeSideEffect() = sideEffect

  override fun dispatch(event: CarScreenEvents) {
    val oldState = state.value
    when (event) {
      CarScreenEvents.AddCar -> launch {
        state.value = oldState.copy(addCarMode = true)
      }
      CarScreenEvents.DismissAddCarDialog -> launch {
        state.value = oldState.copy(addCarMode = false)
      }
      is CarScreenEvents.CreateCar -> launch {
        sideEffect.emit(CarScreenEffects.CreateCar(event.car))
        state.value = oldState.copy(addCarMode = false)
      }
      is CarScreenEvents.InitialDataLoaded -> launch {
        state.value = oldState.copy(cars = event.cars)
      }
    }
  }
}