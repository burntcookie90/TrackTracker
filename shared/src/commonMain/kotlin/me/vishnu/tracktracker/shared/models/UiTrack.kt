package me.vishnu.tracktracker.shared.models

import com.benasher44.uuid.uuid4

data class UiTrack(
  val id: String = uuid4().toString(),
  val name: String,
)