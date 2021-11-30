package me.vishnu.tracktracker.shared.repos

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.db.CarQueries
import me.vishnu.tracktracker.db.TrackQueries
import me.vishnu.tracktracker.shared.models.UiCar
import me.vishnu.tracktracker.shared.models.UiTrack

@Inject
class TrackRepo(private val trackQueries: TrackQueries) {
  fun getAllTracks(): Flow<List<UiTrack>> = trackQueries.selectAll()
    .asFlow()
    .mapToList(Dispatchers.Unconfined)
    .map { tracks ->
      tracks.map { track ->
        UiTrack(
          id = track.id,
          name = track.name
        )
      }
    }
}