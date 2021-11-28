package me.vishnu.tracktracker.shared.graph

import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Scope
import me.vishnu.tracktracker.shared.stores.welcome.WelcomeEffectHandler
import kotlin.annotation.AnnotationTarget.*

@Scope
@Target(CLASS, FUNCTION, PROPERTY_GETTER)
annotation class WelcomeScope

@WelcomeScope
@Component
abstract class WelcomeComponent(@Component val parent: AppComponent) {
  abstract val welcomeEffectHandler: WelcomeEffectHandler
}