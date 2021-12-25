import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.stores.EffectHandler

@Inject
class TurnScreenEffectHandler() : EffectHandler<TurnScreenEffects, TurnScreenEvents>() {
  override val handler: suspend (value: TurnScreenEffects) -> Flow<TurnScreenEvents?> = { effect ->
    consume {  }
  }
}