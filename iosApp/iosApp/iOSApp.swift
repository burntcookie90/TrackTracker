import SwiftUI
import shared

@main
struct iOSApp: App {
	var body: some Scene {
        let store = ObservableStore<WelcomeStore, WelcomeModel, WelcomeEvents, WelcomeEffects>(
            store: WelcomeStore.shared,
            state: WelcomeModel(cars: [UiCar](), addCarMode: false))
		WindowGroup {
            ContentView().environmentObject(store)
		}
	}
}
