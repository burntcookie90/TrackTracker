package me.vishnu.tracktracker.shared.modifier

import com.benasher44.uuid.uuid4

sealed class Modification {
  sealed class Car: Modification() {
    data class CreateCar(
      val year: Int,
      val make: String,
      val model: String,
      val trim: String?,
      val nickname: String?
    ): Car() {
      val id: String = uuid4().toString()
    }
  }

  sealed class Track: Modification() {
    data class CreateTrack(
      val name: String,
      val location: String
    ): Track() {
      val id: String = uuid4().toString()
    }
  }
}
