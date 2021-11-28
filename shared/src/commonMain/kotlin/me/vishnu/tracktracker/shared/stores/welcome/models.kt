package me.vishnu.tracktracker.shared.stores.welcome

import me.vishnu.tracktracker.shared.models.UiCar
import me.vishnu.tracktracker.shared.stores.Event
import me.vishnu.tracktracker.shared.stores.Effect
import me.vishnu.tracktracker.shared.stores.State

data class WelcomeModel(
  val cars: List<UiCar> = emptyList(),
  val addCarMode: Boolean = false
) : State

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