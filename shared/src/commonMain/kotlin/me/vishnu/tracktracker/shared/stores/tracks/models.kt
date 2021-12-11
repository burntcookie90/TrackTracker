package me.vishnu.tracktracker.shared.stores.tracks

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import me.vishnu.tracktracker.shared.models.UiTrack
import me.vishnu.tracktracker.shared.stores.Effect
import me.vishnu.tracktracker.shared.stores.Event
import me.vishnu.tracktracker.shared.stores.Model

data class TrackScreenModel(
  val tracks: List<UiTrack> = emptyList()
) : Model

sealed class TrackScreenEvents : Event {
  data class InitialDataLoaded(val tracks: List<UiTrack>): TrackScreenEvents()
  data class AddTrack(val name: String): TrackScreenEvents()
}

sealed class TrackScreenEffects : Effect {
  object LoadInitialData: TrackScreenEffects()
  data class CreateTrack(val name: String): TrackScreenEffects()
}