import SwiftUI
#if (!targetEnvironment(simulator))
import FirebaseCore
import FirebaseMessaging
#endif
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        
        // Firebase libraries form SPM only work for arm64 but not m1 simulators for undoubtedly good and valid reasons
#if (!targetEnvironment(simulator))
        FirebaseApp.configure()
        UNUserNotificationCenter.current().delegate = self
        
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions,
            completionHandler: { _, _ in }
        )
        
        application.registerForRemoteNotifications()
#endif
        return true
    }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().setAPNSToken(deviceToken, type: .unknown)
        Messaging.messaging().token { token, error in
            if let error = error {
                print("Error fetching FCM registration token: \(error)")
            } else if let token = token {
                print("FCM registration token: \(token)")
                let repo = IosModules().sessionRepository
                repo.postFcmToken(token: token, completionHandler: { response, error in })
            }
        }
    }
}

@available(iOS 16.0, *)
@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    init() {
        IosKoinProviderKt.startKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
