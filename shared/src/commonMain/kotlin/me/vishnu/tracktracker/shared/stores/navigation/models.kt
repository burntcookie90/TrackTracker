import me.vishnu.tracktracker.shared.stores.Effect
import me.vishnu.tracktracker.shared.stores.Event
import me.vishnu.tracktracker.shared.stores.Model

enum class ScreenTarget {
  ROOT, TRACKS, CARS
}
data class NavigationModel(
  val navStack: Set<ScreenTarget> = setOf(ScreenTarget.ROOT)
) : Model {
  companion object {
    fun defaultModel() = NavigationModel()
  }
}

sealed class NavigationEvents : Event {
  data class NavigateTo(val target: ScreenTarget): NavigationEvents()
  object GoBack: NavigationEvents()
}

sealed class NavigationEffects : Effect {
}

typealias OnBackButtonPressed = () -> NavigationEvents.GoBack