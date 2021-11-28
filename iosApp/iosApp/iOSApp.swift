import SwiftUI
import shared

@main
struct iOSApp: App {
    let db = DriverFactoryKt.createDatabase(driverFactory: DriverFactory())
    let obsStore: ObservableStore<WelcomeStore, WelcomeModel, WelcomeEvents, WelcomeEffects>
    let effectHandler : WelcomeEffectHandler
    
    
    init() {
        LoggerKt.doInitLogger()
        let store = WelcomeStore()
        obsStore = ObservableStore<WelcomeStore, WelcomeModel, WelcomeEvents, WelcomeEffects>(
            store: store,
            state: WelcomeModel.Companion.shared.defaultModel(),
            stateWatcher: store.watchState(),
            sideEffectWatcher: store.watchSideEffect()
        )
        effectHandler = WelcomeEffectHandler(
            carRepo: CarRepo(carQueries: db.carQueries),
            modifier: RealDataModifier(database: db))
        
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
