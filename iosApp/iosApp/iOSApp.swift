import SwiftUI
import ComposeApp
import NMapsMap

@main
struct iOSApp: App {
    init() {
        NMFAuthManager.shared().ncpKeyId = NativeBuildKonfig.shared.NAVER_MAP_CLIENT_ID
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
