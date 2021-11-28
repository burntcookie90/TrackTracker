package me.vishnu.tracktracker.shared.models

data class UiCar(
  val year: Int,
  val make: String,
  val model: String,
  val trim: String? = null,
  val nickname: String? = null
)

fun UiCar.titleDisplay() = "$year $make $model $trim"