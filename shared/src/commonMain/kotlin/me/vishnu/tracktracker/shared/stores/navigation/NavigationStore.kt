import io.github.aakira.napier.Napier
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.stores.ActualStore

@Inject
class NavigationStore :
  ActualStore<NavigationModel, NavigationEvents, NavigationEffects>(NavigationModel()) {
  override fun update(model: NavigationModel, event: NavigationEvents) {
    when (event) {
      is NavigationEvents.NavigateTo -> {
        val navStack = model.navStack.toMutableList()

        if (navStack.last() != event.target) {
          if (navStack.contains(event.target)) {
            navStack.remove(event.target)
          }
          navStack.add(event.target)
          next(model.copy(navStack = navStack))
        }
      }
      is NavigationEvents.GoBack -> {
        val navStack = model.navStack.toMutableList()
        if (navStack.size != 1 || navStack.last() != ScreenTarget.ROOT) {
          navStack.remove(navStack.last())
          next(model.copy(navStack = navStack))
        }
      }
    }
  }
}