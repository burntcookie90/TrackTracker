import SwiftUI
import shared

@main
struct iOSApp: App {
    typealias ObservableWelcomeStore = ObservableStore<WelcomeStore, WelcomeModel, WelcomeEvents, WelcomeEffects>
    let obsStore: ObservableWelcomeStore
    let component = InjectAppComponent(driverFactory: DriverFactory())
    
    init() {
        let welcomeComponent = InjectWelcomeComponent(parent: component)
        let store = welcomeComponent.welcomeStore
        let effectHandler = welcomeComponent.welcomeEffectHandler
        obsStore = ObservableWelcomeStore(
            store: store,
            state: WelcomeModel.Companion.shared.defaultModel(),
            stateWatcher: store.watchState(),
            sideEffectWatcher: store.watchSideEffect()
        )
        
        _ = Loop<WelcomeModel, WelcomeEvents, WelcomeEffects, WelcomeEffectHandler>(store: store, effectHandler: effectHandler) {
            let initEffects : Set = [WelcomeEffects.LoadInitialData.shared]
            return initEffects
        }
    }
	var body: some Scene {
        
		WindowGroup {
            WelcomeScreen().environmentObject(obsStore)
		}
	}
}
