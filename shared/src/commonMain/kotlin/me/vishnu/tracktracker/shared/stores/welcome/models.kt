package me.vishnu.tracktracker.shared.stores.welcome

import me.vishnu.tracktracker.shared.models.UiCar
import me.vishnu.tracktracker.shared.stores.Event
import me.vishnu.tracktracker.shared.stores.Effect
import me.vishnu.tracktracker.shared.stores.Model

data class CarScreenModel(
  val cars: List<UiCar> = emptyList(),
  val addCarMode: Boolean = false,
  val selectableYears: List<Int> = (1950..2022).reversed().toList()
) : Model {
  companion object {
    fun defaultModel() = CarScreenModel()
  }
}


sealed class CarScreenEvents : Event {
  object AddCar : CarScreenEvents()

  object DismissAddCarDialog: CarScreenEvents()

  data class InitialDataLoaded(val cars: List<UiCar>) : CarScreenEvents()

  data class CreateCar(val car: UiCar) : CarScreenEvents()
}

sealed class CarScreenEffects : Effect {
  object LoadInitialData : CarScreenEffects()

  data class CreateCar(val car: UiCar) : CarScreenEffects()
}