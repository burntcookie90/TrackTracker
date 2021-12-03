package me.vishnu.tracktracker.shared.stores.tracks

import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.stores.ActualStore

@Inject
class TrackScreenStore : ActualStore<TrackScreenModel, TrackScreenEvents, TrackScreenEffects>(TrackScreenModel()) {
  override fun update(model: TrackScreenModel, event: TrackScreenEvents) {
    TODO("Not yet implemented")
  }
}