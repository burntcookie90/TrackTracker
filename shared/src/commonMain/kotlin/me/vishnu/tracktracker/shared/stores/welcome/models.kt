package me.vishnu.tracktracker.shared.stores.welcome

import me.vishnu.tracktracker.shared.models.UiCar
import me.vishnu.tracktracker.shared.stores.Event
import me.vishnu.tracktracker.shared.stores.Effect
import me.vishnu.tracktracker.shared.stores.Model

data class WelcomeModel(
  val cars: List<UiCar> = emptyList(),
  val addCarMode: Boolean = false,
  val selectableYears: List<Int> = (1950..2022).reversed().toList()
) : Model {
  companion object {
    fun defaultModel() = WelcomeModel()
  }
}


sealed class WelcomeEvents : Event {
  object AddCar : WelcomeEvents()

  object DismissAddCarDialog: WelcomeEvents()

  data class InitialDataLoaded(val cars: List<UiCar>) : WelcomeEvents()

  data class CreateCar(val car: UiCar) : WelcomeEvents()
}

sealed class WelcomeEffects : Effect {
  object LoadInitialData : WelcomeEffects()

  data class CreateCar(val car: UiCar) : WelcomeEffects()
}