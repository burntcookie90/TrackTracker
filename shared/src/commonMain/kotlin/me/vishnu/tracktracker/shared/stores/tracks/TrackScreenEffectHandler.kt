package me.vishnu.tracktracker.shared.stores.tracks

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.modifier.DataModifier
import me.vishnu.tracktracker.shared.repos.TrackRepo
import me.vishnu.tracktracker.shared.stores.EffectHandler

@Inject
class TrackScreenEffectHandler(
  private val trackRepo: TrackRepo,
  private val modifier: DataModifier,
) : EffectHandler<TrackScreenEffects, TrackScreenEvents>() {

  override val handler: suspend (value: TrackScreenEffects) -> Unit = { effect ->
    when (effect) {
      TrackScreenEffects.LoadInitialData -> loadInitialData(effect as TrackScreenEffects.LoadInitialData)
    }
  }

  private fun loadInitialData(effect: TrackScreenEffects.LoadInitialData) {
    launch {
      trackRepo.getAllTracks()
        .collect { dispatch(TrackScreenEvents.InitialDataLoaded(tracks= it)) }
    }
  }
}