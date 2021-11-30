package me.vishnu.tracktracker.shared.model

import me.vishnu.tracktracker.shared.models.UiCar

fun uiCar(
  year: Int,
  make: String,
  model: String,
  trim: String? = null,
  nickname: String? = null
) = UiCar(
  year = year,
  make = make,
  model = model,
  trim = trim,
  nickname = nickname
)
