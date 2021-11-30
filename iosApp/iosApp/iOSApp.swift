import SwiftUI
import shared

@main
struct iOSApp: App {
  
  let component = InjectAppComponent(driverFactory: DriverFactory())
  var body: some Scene {
    WindowGroup {
      carScreen(component: component)
    }
  }
}
