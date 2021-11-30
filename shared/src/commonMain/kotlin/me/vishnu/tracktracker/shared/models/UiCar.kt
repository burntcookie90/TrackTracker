package me.vishnu.tracktracker.shared.models

import com.benasher44.uuid.uuid4

data class UiCar(
  val id: String = uuid4().toString(),
  val year: Int,
  val make: String,
  val model: String,
  val trim: String? = null,
  val nickname: String? = null
)

fun UiCar.titleDisplay() = "$year $make $model $trim"