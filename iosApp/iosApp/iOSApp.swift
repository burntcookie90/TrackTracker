import SwiftUI
import shared

@main
struct iOSApp: App {
  typealias ObservableCarScreen = ObservableStore<CarScreenStore, CarScreenModel, CarScreenEvents, CarScreenEffects>
  let obsStore: ObservableCarScreen
  let component = InjectAppComponent(driverFactory: DriverFactory())
  
  init() {
    let carScreenComponent = InjectCarScreenComponent(parent: component)
    let store = carScreenComponent.carScreenStore
    let effectHandler = carScreenComponent.carScreenEffectHandler
    obsStore = ObservableCarScreen(
      store: store,
      state: CarScreenModel.Companion.shared.defaultModel(),
      stateWatcher: store.watchState(),
      sideEffectWatcher: store.watchSideEffect()
    )
    
    _ = Loop<CarScreenModel, CarScreenEvents, CarScreenEffects, CarScreenEffectHandler>(store: store, effectHandler: effectHandler) {
      let initEffects : Set = [CarScreenEffects.LoadInitialData.shared]
      return initEffects
    }
  }
  var body: some Scene {
    
    WindowGroup {
      CarScreen().environmentObject(obsStore)
    }
  }
}
