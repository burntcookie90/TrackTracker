package me.vishnu.tracktracker.shared

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import me.vishnu.tracktracker.db.Database

actual class DriverFactory(private val context: Context) {
  actual fun createDriver(): SqlDriver =
    AndroidSqliteDriver(Database.Schema, context, "tracktracker.db")
}