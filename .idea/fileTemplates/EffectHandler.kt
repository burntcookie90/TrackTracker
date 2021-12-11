import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.stores.EffectHandler

@Inject
class ${SCREEN_NAME}EffectHandler() : EffectHandler<${SCREEN_NAME}Effects, ${SCREEN_NAME}Events>() {
  override val handler: suspend (value: ${SCREEN_NAME}Effects) -> ${ScreenName}Events = { effect ->
    when (effect) {
    }
  }
}