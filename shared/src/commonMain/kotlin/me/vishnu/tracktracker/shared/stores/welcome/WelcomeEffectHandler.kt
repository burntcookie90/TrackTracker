package me.vishnu.tracktracker.shared.stores.welcome

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.modifier.DataModifier
import me.vishnu.tracktracker.shared.modifier.Modification
import me.vishnu.tracktracker.shared.repos.CarRepo
import me.vishnu.tracktracker.shared.stores.EffectHandler

@Inject
class WelcomeEffectHandler(
  private val carRepo: CarRepo,
  private val modifier: DataModifier,
) : EffectHandler<WelcomeEffects, WelcomeEvents>() {

  override val handler: suspend (value: WelcomeEffects) -> Unit = { effect ->
    when (effect) {
      is WelcomeEffects.CreateCar -> createCar(effect)
      WelcomeEffects.LoadInitialData -> loadInitialData(effect as WelcomeEffects.LoadInitialData)
    }
  }

  private fun loadInitialData(effect: WelcomeEffects.LoadInitialData) {
    launch {
      carRepo.getAllCars()
        .collect { dispatch(WelcomeEvents.InitialDataLoaded(cars = it)) }
    }
  }

  private fun createCar(effect: WelcomeEffects.CreateCar) {
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