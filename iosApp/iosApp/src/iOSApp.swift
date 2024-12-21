import Foundation
import SwiftUI
#if (!targetEnvironment(simulator))
import FirebaseCore
import FirebaseMessaging
import PostHog
#endif
import ComposeApp

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    
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
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    init() {
        initSentry()
        initPosthog()
        IosKoinProviderKt.startKoin(appPriceProvider: IosAppPriceProvider(), fileExporter: IosFileExporter())
        
        // Check app purchase wasnt refunded or subscription cancelled
        let iapRepository: IapRepository = IosModules().iapRepository
        Task {
            do {
                try await iapRepository.restorePurchase()
            } catch {
                
            }
        }
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
#if (!targetEnvironment(simulator))
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
#endif
    }
}
