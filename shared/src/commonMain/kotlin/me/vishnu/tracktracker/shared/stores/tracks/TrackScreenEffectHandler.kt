package me.vishnu.tracktracker.shared.stores.tracks

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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

  override val handler: suspend (value: TrackScreenEffects) -> Flow<TrackScreenEvents?> = { effect ->
    when (effect) {
      is TrackScreenEffects.LoadInitialData -> loadInitialData(effect)
      is TrackScreenEffects.CreateTrack -> createTrack(effect)
    }
  }

  private suspend fun loadInitialData(effect: TrackScreenEffects.LoadInitialData) =
    trackRepo.getAllTracks()
      .map { TrackScreenEvents.InitialDataLoaded(tracks = it) }

  private fun createTrack(effect: TrackScreenEffects.CreateTrack) = consume {
    modifier.submit(Modification.Track.CreateTrack(effect.name, effect.location))
  }
}