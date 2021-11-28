package me.vishnu.tracktracker.shared

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import me.vishnu.tracktracker.db.Database

actual class DriverFactory {
  actual fun createDriver(): SqlDriver = NativeSqliteDriver(Database.Schema, "tracktracker.db")
}