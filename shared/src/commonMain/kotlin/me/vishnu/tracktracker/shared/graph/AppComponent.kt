package me.vishnu.tracktracker.shared.graph

import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope
import me.vishnu.tracktracker.db.CarQueries
import me.vishnu.tracktracker.db.Database
import me.vishnu.tracktracker.shared.DriverFactory
import me.vishnu.tracktracker.shared.createDatabase
import me.vishnu.tracktracker.shared.initLogger
import me.vishnu.tracktracker.shared.modifier.DataModifier
import me.vishnu.tracktracker.shared.modifier.RealDataModifier
import me.vishnu.tracktracker.shared.repos.CarRepo

@Scope
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
annotation class AppScope

@AppScope
@Component
abstract class AppComponent(@get:Provides val driverFactory: DriverFactory) {
  abstract val carRepo: CarRepo
  abstract val dataModifier: DataModifier

  init {
    initLogger()
  }


  @Provides fun db(driverFactory: DriverFactory): Database = createDatabase(driverFactory)
  @Provides fun carQueries(db: Database): CarQueries = db.carQueries
  protected val RealDataModifier.bind: DataModifier @Provides get() = this
}