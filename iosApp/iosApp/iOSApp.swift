import SwiftUI
import shared

@main
struct iOSApp: App {
    let obsStore: ObservableStore<WelcomeStore, WelcomeModel, WelcomeEvents, WelcomeEffects>
    
    init() {
        LoggerKt.doInitLogger()
        let db = DriverFactoryKt.createDatabase(driverFactory: DriverFactory())
        let component = InjectAppComponent(db: db)
        let welcomeComponent = InjectWelcomeComponent(parent: component)
        let store = WelcomeStore()
        obsStore = ObservableStore<WelcomeStore, WelcomeModel, WelcomeEvents, WelcomeEffects>(
            store: store,
            state: WelcomeModel.Companion.shared.defaultModel(),
            stateWatcher: store.watchState(),
            sideEffectWatcher: store.watchSideEffect()
        )
        let effectHandler = welcomeComponent.welcomeEffectHandler
        effectHandler.bindToStore(effectFlow: store.observeSideEffect()) { event in
            store.dispatch(event: event)
        }
        
        store.start {
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
