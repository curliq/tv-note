import SwiftUI
#if (!targetEnvironment(simulator))
import FirebaseCore
import FirebaseMessaging
#endif
import ComposeApp
import PostHog

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        
        // Firebase libraries from SPM only work for arm64 but not m1 simulators for undoubtedly good and valid reasons
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
#if (!targetEnvironment(simulator))
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
#endif
    }
}

@available(iOS 16.0, *)
@main
struct iOSApp: App {
    init() {
        initSentry()
        initPosthog()
        IosKoinProviderKt.startKoin()
    }
    var body: some Scene {
        WindowGroup {
            SplashScreen()
        }
    }
    
    private func initSentry() {
        if let url = Bundle.main.url(forResource: "Secrets", withExtension: "plist"),
           let data = try? Data(contentsOf: url),
           let plist = try? PropertyListSerialization.propertyList(from: data, options: [], format: nil) as? [String: Any] {
            let apiKey = plist["SENTRY_DSN"] as? String
            if let apiKey {
                SentryLogging_iosKt.doInitSentry(dsn: apiKey)
            }
        }
    }
    
    private func initPosthog() {
        if let url = Bundle.main.url(forResource: "Secrets", withExtension: "plist"),
           let data = try? Data(contentsOf: url),
           let plist = try? PropertyListSerialization.propertyList(from: data, options: [], format: nil) as? [String: Any] {
            let apiKey = plist["POSTHOG_KEY"] as? String
            if let apiKey {
                let POSTHOG_HOST = "https://eu.i.posthog.com"
                let config = PostHogConfig(apiKey: apiKey, host: POSTHOG_HOST)
                PostHogSDK.shared.setup(config)
            }
        }
        
    }
    
}
