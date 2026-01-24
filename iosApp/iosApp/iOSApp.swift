import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    let iosAppGraph = IosAppGraphKt.createIosAppGraph()

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}