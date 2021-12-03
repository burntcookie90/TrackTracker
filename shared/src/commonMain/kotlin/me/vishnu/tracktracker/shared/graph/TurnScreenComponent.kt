package me.vishnu.tracktracker.shared.graph

import TurnScreenEffectHandler
import TurnScreenStore
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Scope

@Scope
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
annotation class TurnScreenScope

@TurnScreenScope
@Component
abstract class TurnScreenComponent(@Component val parent: AppComponent) {
  abstract val turnScreenStore: TurnScreenStore @TurnScreenScope get
  abstract val turnScreenEffectHandler: TurnScreenEffectHandler @TurnScreenScope get
}