package me.vishnu.tracktracker.shared.stores.tracks

import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.stores.ActualStore

@Inject
class TrackScreenStore :
  ActualStore<TrackScreenModel, TrackScreenEvents, TrackScreenEffects>(TrackScreenModel()) {
  override fun update(model: TrackScreenModel, event: TrackScreenEvents) = when (event) {
    is TrackScreenEvents.InitialDataLoaded -> next(model.copy(tracks = event.tracks))
    is TrackScreenEvents.AddTrack -> next(model.copy(addTrackModel = true))
    is TrackScreenEvents.CreateTrack -> noChange()
    TrackScreenEvents.DismissAddTrack -> next(model.copy(addTrackModel = false))
  }
}