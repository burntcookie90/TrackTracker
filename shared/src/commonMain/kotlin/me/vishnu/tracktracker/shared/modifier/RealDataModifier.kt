package me.vishnu.tracktracker.shared.modifier

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.db.CarQueries
import me.vishnu.tracktracker.db.Database
import me.vishnu.tracktracker.db.TrackQueries

@Inject
class RealDataModifier(
  private val carQueries: CarQueries,
  private val trackQueries: TrackQueries,
  ) : DataModifier,
  CoroutineScope by CoroutineScope(Dispatchers.Default) {

  override fun submit(mod: Modification) = when (mod) {
    is Modification.Car.CreateCar -> createCar(mod)
    is Modification.Track.CreateTrack -> createTrack(mod)
  }

  private fun createCar(mod: Modification.Car.CreateCar) {
    launch {
      carQueries.insertNewCar(
        id = mod.id,
        year = mod.year.toLong(),
        make = mod.make,
        model = mod.model,
        trim = mod.trim,
        nickname = mod.nickname
      )
    }
  }

  private fun createTrack(mod: Modification.Track.CreateTrack) {
    launch {
      trackQueries.insertNewTrack(
        id = mod.id,
        name = mod.name,
        location = mod.location
      )
    }
  }
}