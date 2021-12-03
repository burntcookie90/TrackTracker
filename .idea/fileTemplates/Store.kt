import me.tatarka.inject.annotations.Inject
import me.vishnu.tracktracker.shared.stores.ActualStore

@Inject
class ${SCREEN_NAME}Store : ActualStore<${SCREEN_NAME}Model, ${SCREEN_NAME}Events, ${SCREEN_NAME}Effects>(${SCREEN_NAME}Model()) {
  override fun update(model: ${SCREEN_NAME}Model, event: ${SCREEN_NAME}Events) {
    TODO("Not yet implemented")
  }
}