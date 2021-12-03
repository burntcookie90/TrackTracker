package me.vishnu.tracktracker.shared.stores.welcome

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.stores.ActualStore

@Inject
class CarScreenStore : ActualStore<CarScreenModel, CarScreenEvents, CarScreenEffects>(CarScreenModel()) {
  override fun update(model: CarScreenModel, event: CarScreenEvents) {
    when (event) {
      CarScreenEvents.AddCar -> next(model.copy(addCarMode = true))
      CarScreenEvents.DismissAddCarDialog -> next(model.copy(addCarMode = false))
      is CarScreenEvents.CreateCar -> next(
        model.copy(addCarMode = false),
        CarScreenEffects.CreateCar(event.car)
      )
      is CarScreenEvents.InitialDataLoaded -> next(model.copy(cars = event.cars))
    }
  }
}