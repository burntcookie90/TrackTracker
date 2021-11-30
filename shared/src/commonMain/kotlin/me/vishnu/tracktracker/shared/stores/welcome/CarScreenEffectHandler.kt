package me.vishnu.tracktracker.shared.stores.welcome

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.modifier.DataModifier
import me.vishnu.tracktracker.shared.modifier.Modification
import me.vishnu.tracktracker.shared.repos.CarRepo
import me.vishnu.tracktracker.shared.stores.EffectHandler

@Inject
class CarScreenEffectHandler(
  private val carRepo: CarRepo,
  private val modifier: DataModifier,
) : EffectHandler<CarScreenEffects, CarScreenEvents>() {

  override val handler: suspend (value: CarScreenEffects) -> Unit = { effect ->
    when (effect) {
      is CarScreenEffects.CreateCar -> createCar(effect)
      CarScreenEffects.LoadInitialData -> loadInitialData(effect as CarScreenEffects.LoadInitialData)
    }
  }

  private fun loadInitialData(effect: CarScreenEffects.LoadInitialData) {
    launch {
      carRepo.getAllCars()
        .collect { dispatch(CarScreenEvents.InitialDataLoaded(cars = it)) }
    }
  }

  private fun createCar(effect: CarScreenEffects.CreateCar) {
    modifier.submit(
      Modification.Car.CreateCar(
        year = effect.car.year,
        make = effect.car.make,
        model = effect.car.model,
        trim = effect.car.trim,
        nickname = effect.car.nickname
      )
    )
  }
}