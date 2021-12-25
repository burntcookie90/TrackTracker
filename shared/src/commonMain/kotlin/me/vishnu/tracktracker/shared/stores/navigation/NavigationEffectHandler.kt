import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.stores.EffectHandler

@Inject
class NavigationEffectHandler() :
  EffectHandler<NavigationEffects, NavigationEvents>() {
  override val handler: suspend (value: NavigationEffects) -> Flow<NavigationEvents?> = { effect ->
    consume { }
  }
}