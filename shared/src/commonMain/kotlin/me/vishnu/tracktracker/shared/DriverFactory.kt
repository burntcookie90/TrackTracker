package me.vishnu.tracktracker.shared

import com.squareup.sqldelight.db.SqlDriver
import me.vishnu.tracktracker.db.Database

expect class DriverFactory {
  fun createDriver(): SqlDriver
}
