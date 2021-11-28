package me.vishnu.tracktracker.shared

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun initLogger() {
  Napier.base(DebugAntilog())
}