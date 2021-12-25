import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.stores.ActualStore

@Inject
class TurnScreenStore :
  ActualStore<TurnScreenModel, TurnScreenEvents, TurnScreenEffects>(TurnScreenModel()) {
  override fun update(model: TurnScreenModel, event: TurnScreenEvents) = noChange()
}