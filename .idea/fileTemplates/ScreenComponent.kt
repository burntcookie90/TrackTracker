#set($functionName = $SCREEN_NAME.substring(0,1).toLowerCase() + $SCREEN_NAME.substring(1))


package me.vishnu.tracktracker.shared.graph

import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Scope

@Scope
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
annotation class ${SCREEN_NAME}Scope

@${SCREEN_NAME}Scope
@Component
abstract class ${SCREEN_NAME}Component(@Component val parent: AppComponent) {
  abstract val ${functionName}Store: ${SCREEN_NAME}Store @${SCREEN_NAME}Scope get
  abstract val ${functionName}EffectHandler: ${SCREEN_NAME}EffectHandler @${SCREEN_NAME}Scope get
}