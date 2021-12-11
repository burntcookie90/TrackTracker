package me.vishnu.tracktracker.shared.stores.welcome

import kotlinx.coroutines.flow.first
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

  override val handler: suspend (value: CarScreenEffects) -> CarScreenEvents? = { effect ->
    when (effect) {
      is CarScreenEffects.CreateCar -> createCar(effect)
      is CarScreenEffects.LoadInitialData -> loadInitialData(effect)
    }
  }

  private suspend fun loadInitialData(effect: CarScreenEffects.LoadInitialData) =
    carRepo.getAllCars()
      .first()
      .let {
        CarScreenEvents.InitialDataLoaded(it)
      }

  private fun createCar(effect: CarScreenEffects.CreateCar) = consume{
    modifier.submit(
      Modification.Car.CreateCar(
        year = effect.year,
        make = effect.make,
        model = effect.model,
        trim = effect.trim,
        nickname = effect.nickname
      )
    )
  }
}