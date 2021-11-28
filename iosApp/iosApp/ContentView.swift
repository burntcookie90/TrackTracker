import SwiftUI
import shared

struct ContentView: View {
    @EnvironmentObject var store: ObservableStore<WelcomeStore, WelcomeModel, WelcomeEvents, WelcomeEffects>
	let greet = Greeting().greeting()
//    let db = DriverFactoryKt.createDatabase(driverFactory: DriverFactory())
//
//    let store = WelcomeStore()
    
//    let effectHandler = WelcomeEffectHandler(
//        carRepo: CarRepo(carQueries: db.carQueries),
//        modifier: RealDataModifier(database: db))
//    let loopResult = StoreKt.Loop(store: Store, effectHandler: EffectHandler, init: {Set})
    
	var body: some View {
        Text(greet)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
