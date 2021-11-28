package me.vishnu.tracktracker.shared.modifier

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.db.CarQueries
import me.vishnu.tracktracker.db.Database

@Inject
class RealDataModifier(database: Database) : DataModifier,
  CoroutineScope by CoroutineScope(Dispatchers.Unconfined) {
  private val carQueries = database.carQueries


  override fun submit(mod: Modification) = when (mod) {
    is Modification.Car.CreateCar -> createCar(mod)
  }

  private fun createCar(mod: Modification.Car.CreateCar) {
    launch {
      carQueries.insertNewCar(
        year = mod.year.toLong(),
        make = mod.make,
        model = mod.model,
        trim = mod.trim,
        nickname = mod.nickname
      )
    }
  }
}