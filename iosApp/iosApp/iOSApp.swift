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
        let stateWatcher = store.watchState()
        
        let sideEffectWatcher = store.watchSideEffect()
        obsStore = ObservableStore<WelcomeStore, WelcomeModel, WelcomeEvents, WelcomeEffects>(
            store: WelcomeStore(),
            state: WelcomeModel(cars: [UiCar](), addCarMode: false),
            stateWatcher: stateWatcher,
            sideEffectWatcher: sideEffectWatcher
        )
        effectHandler = WelcomeEffectHandler(
            carRepo: CarRepo(carQueries: db.carQueries),
            modifier: RealDataModifier(database: db))
        
        effectHandler.bindToStore(effectFlow: obsStore.store.observeSideEffect()) { event in
            store.dispatch(event: event)
        }
        
        obsStore.store.start {
            let initEffects : Set = [WelcomeEffects.LoadInitialData.shared]
            return initEffects
        }
        
    }
	var body: some Scene {
        
		WindowGroup {
            ContentView().environmentObject(obsStore)
		}
	}
}
