import ComposeApp
import UIKit

class AppDelegate: NSObject, UIApplicationDelegate {
    private lazy var festabookAppDelegate = DefaultFestabookAppDelegate.shared

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        let result = festabookAppDelegate.application(application: application, launchOptions: launchOptions)

        application.registerForRemoteNotifications()

        return result
    }

    func application(_ application: UIApplication, supportedInterfaceOrientationsFor window: UIWindow?) -> UIInterfaceOrientationMask {
        return .portrait
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        festabookAppDelegate.application(application: application, deviceToken: deviceToken)
    }

    func application(
        _ application: UIApplication,
        didReceiveRemoteNotification userInfo: [AnyHashable: Any],
        fetchCompletionHandler: @escaping (UIBackgroundFetchResult) -> Void
    ) {
        festabookAppDelegate.application(application: application, userInfo: userInfo) { result in
            fetchCompletionHandler(UIBackgroundFetchResult(rawValue: UInt(result.value.uint64Value)) ?? .noData)
        }
    }
}
