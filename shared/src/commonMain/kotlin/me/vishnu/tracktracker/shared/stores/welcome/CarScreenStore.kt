package me.vishnu.tracktracker.shared.stores.welcome

import com.benasher44.uuid.uuid4
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.stores.ActualStore

@Inject
class CarScreenStore :
  ActualStore<CarScreenModel, CarScreenEvents, CarScreenEffects>(CarScreenModel()) {
  override fun update(model: CarScreenModel, event: CarScreenEvents) = when (event) {
    CarScreenEvents.AddCar -> {
//      next(model.copy(addCarMode = true))
      next(
        CarScreenEffects.CreateCar(
          year = model.selectableYears.random(),
          make = uuid4().toString().take(8),
          model = uuid4().toString().take(8),
          trim = uuid4().toString().take(8),
          nickname = uuid4().toString().take(8)
        )
      )
    }
    CarScreenEvents.DismissAddCarDialog -> next(model.copy(addCarMode = false))
    is CarScreenEvents.CreateCar -> next(
      model.copy(addCarMode = false),
      CarScreenEffects.CreateCar(
        year = event.year,
        make = event.make,
        model = event.model,
        trim = event.trim,
        nickname = event.nickname
      )
    )
    is CarScreenEvents.InitialDataLoaded -> next(model.copy(cars = event.cars))
  }
}