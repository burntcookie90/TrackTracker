import me.vishnu.tracktracker.shared.models.UiTurn
import me.vishnu.tracktracker.shared.stores.Effect
import me.vishnu.tracktracker.shared.stores.Event
import me.vishnu.tracktracker.shared.stores.Model

data class TurnScreenModel(
  val turns: List<UiTurn> = emptyList()
) : Model

sealed class TurnScreenEvents : Event {
}

sealed class TurnScreenEffects : Effect {
}