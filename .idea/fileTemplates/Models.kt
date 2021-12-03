import me.vishnu.tracktracker.shared.stores.Effect
import me.vishnu.tracktracker.shared.stores.Event
import me.vishnu.tracktracker.shared.stores.Model

data class ${SCREEN_NAME}Model() : Model

sealed class ${SCREEN_NAME}Events : Event {
}

sealed class ${SCREEN_NAME}Effects : Effect {
}