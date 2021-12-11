package me.vishnu.tracktracker.shared.graph

import NavigationEffectHandler
import NavigationStore
import OnBackButtonPressed
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import me.tatarka.inject.annotations.Scope
import me.vishnu.tracktracker.db.CarQueries
import me.vishnu.tracktracker.db.Database
import me.vishnu.tracktracker.db.TrackQueries
import me.vishnu.tracktracker.shared.DriverFactory
import me.vishnu.tracktracker.shared.initLogger
import me.vishnu.tracktracker.shared.modifier.DataModifier
import me.vishnu.tracktracker.shared.modifier.RealDataModifier

@Scope
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
annotation class AppScope

@AppScope
@Component
abstract class AppComponent(
  @get:Provides val driverFactory: DriverFactory,
) {

  init {
    initLogger()
  }

  @Provides @AppScope
  fun db(driverFactory: DriverFactory): Database = Database(driverFactory.createDriver())

  @Provides @AppScope fun carQueries(db: Database): CarQueries = db.carQueries
  @Provides @AppScope fun trackQueries(db: Database): TrackQueries = db.trackQueries

  internal val RealDataModifier.bind: DataModifier @Provides @AppScope get() = this


  abstract val navigationStore: NavigationStore @AppScope get
  abstract val navigationEffectHandler: NavigationEffectHandler @AppScope get
}