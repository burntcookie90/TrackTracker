package me.vishnu.tracktracker.shared.repos

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.db.CarQueries
import me.vishnu.tracktracker.shared.models.UiCar

@Inject
class CarRepo(private val carQueries: CarQueries) {
  fun getAllCars(): Flow<List<UiCar>> =
    carQueries.selectAll { id, year, make, model, trim, nickname, note_id ->
      UiCar(id, year.toInt(), make, model, trim, nickname)
    }
      .asFlow()
      .mapToList()
}