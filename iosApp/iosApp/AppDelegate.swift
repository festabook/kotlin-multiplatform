import FirebaseCore
import ComposeApp
import NMapsMap

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil
    ) -> Bool {
        FirebaseApp.configure()
        NMFAuthManager.shared().ncpKeyId = NativeBuildKonfig.shared.NAVER_MAP_CLIENT_ID
        return true
    }
}
