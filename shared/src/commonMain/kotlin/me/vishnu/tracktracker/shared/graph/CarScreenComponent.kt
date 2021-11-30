package me.vishnu.tracktracker.shared.graph

import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Scope
import me.vishnu.tracktracker.shared.stores.welcome.CarScreenEffectHandler
import me.vishnu.tracktracker.shared.stores.welcome.CarScreenStore
import kotlin.annotation.AnnotationTarget.*

@Scope
@Target(CLASS, FUNCTION, PROPERTY_GETTER)
annotation class CarScreenScope

@CarScreenScope
@Component
abstract class CarScreenComponent(@Component val parent: AppComponent) {
  abstract val carScreenStore: CarScreenStore @CarScreenScope get
  abstract val carScreenEffectHandler: CarScreenEffectHandler @CarScreenScope get
}