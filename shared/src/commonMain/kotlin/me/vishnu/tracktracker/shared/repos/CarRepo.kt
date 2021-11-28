package me.vishnu.tracktracker.shared.repos

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.db.CarQueries
import me.vishnu.tracktracker.shared.models.UiCar

@Inject
class CarRepo(private val carQueries: CarQueries) {
  fun getAllCars(): Flow<List<UiCar>> = carQueries.selectAll()
    .asFlow()
    .mapToList(Dispatchers.Unconfined)
    .map { cars ->
      cars.map { car ->
        UiCar(
          year = car.year.toInt(),
          make = car.make,
          model = car.model,
          trim = car.trim,
          nickname = car.nickname
        )
      }
    }
}