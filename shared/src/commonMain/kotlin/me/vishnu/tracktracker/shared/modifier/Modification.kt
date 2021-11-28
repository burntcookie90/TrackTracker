package me.vishnu.tracktracker.shared.modifier

sealed class Modification {
  sealed class Car: Modification() {
    data class CreateCar(
      val year: Int,
      val make: String,
      val model: String,
      val trim: String?,
      val nickname: String?
    ): Car()
  }
}
