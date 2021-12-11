package me.vishnu.tracktracker.shared.stores.tracks

import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.modifier.DataModifier
import me.vishnu.tracktracker.shared.modifier.Modification
import me.vishnu.tracktracker.shared.repos.TrackRepo
import me.vishnu.tracktracker.shared.stores.EffectHandler

@Inject
class TrackScreenEffectHandler(
  private val trackRepo: TrackRepo,
  private val modifier: DataModifier,
) : EffectHandler<TrackScreenEffects, TrackScreenEvents>() {

  override val handler: suspend (value: TrackScreenEffects) -> TrackScreenEvents? = { effect ->
    when (effect) {
      is TrackScreenEffects.LoadInitialData -> loadInitialData(effect)
      is TrackScreenEffects.CreateTrack -> createTrack(effect)
    }
  }

  private suspend fun loadInitialData(effect: TrackScreenEffects.LoadInitialData) =
    trackRepo.getAllTracks()
      .first()
      .let { TrackScreenEvents.InitialDataLoaded(tracks = it) }

  private fun createTrack(effect: TrackScreenEffects.CreateTrack) = consume {
    modifier.submit(Modification.Track.CreateTrack(effect.name))
  }
}