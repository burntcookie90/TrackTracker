package me.vishnu.tracktracker.shared.graph

import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Scope
import me.vishnu.tracktracker.shared.stores.tracks.TrackScreenEffectHandler
import me.vishnu.tracktracker.shared.stores.tracks.TrackScreenStore

@Scope
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
annotation class TrackScreenScope

@TrackScreenScope
@Component
abstract class TrackScreenComponent(@Component val parent: AppComponent) {
  abstract val trackScreenStore: TrackScreenStore @TrackScreenScope get
  abstract val trackScreenEffectHandler: TrackScreenEffectHandler @TrackScreenScope get
}