package me.vishnu.tracktracker.shared.stores.tracks

import me.vishnu.tracktracker.shared.models.UiTrack
import me.vishnu.tracktracker.shared.stores.Effect
import me.vishnu.tracktracker.shared.stores.Event
import me.vishnu.tracktracker.shared.stores.Model

data class TrackScreenModel(
  val tracks: List<UiTrack> = emptyList()
) : Model

sealed class TrackScreenEvents : Event {

}

sealed class TrackScreenEffects : Effect {

}