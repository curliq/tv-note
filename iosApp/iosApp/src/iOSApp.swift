import SwiftUI
#if (!targetEnvironment(simulator))
import FirebaseCore
import FirebaseMessaging
import PostHog
#endif
import ComposeApp
import StoreKit

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

class IosFileExporter: FileExporter {
    
    // this is all AI copy paste
    
    func export(content: String, fileName: String) {
        if let fileURL = createTextFile(with: content, fileName: fileName) {
            shareTextFile(fileURL: fileURL)
        } else {
            print("error export file")
        }
    }
    
    func createTextFile(with content: String, fileName: String) -> URL? {
        // Get the document directory URL
        guard let documentDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first else {
            print("Could not find document directory")
            return nil
        }
        
        // Create the file URL
        let fileURL = documentDirectory.appendingPathComponent(fileName)
        
        do {
            // Write the content to the file
            try content.write(to: fileURL, atomically: true, encoding: .utf8)
            return fileURL
        } catch {
            print("Error creating file: \(error)")
            return nil
        }
    }
    
    func shareTextFile(fileURL: URL) {
        let activityViewController = UIActivityViewController(activityItems: [fileURL], applicationActivities: nil)
        
        // Present the activity view controller
        DispatchQueue.main.async {
            // Find the topmost view controller
            guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
                  let rootViewController = windowScene.windows.first?.rootViewController else {
                print("Could not find root view controller")
                return
            }
            
            var topViewController = rootViewController
            while let presentedViewController = topViewController.presentedViewController {
                topViewController = presentedViewController
            }
            
            topViewController.present(activityViewController, animated: true, completion: nil)
        }
    }
}

class IosAppPriceProvider: AppPriceProvider {

    func buyApp(completionHandler: @escaping (KotlinBoolean?, (any Error)?) -> Void) {
        Task {
            do {
                let product = try await Product.products(for: ["01"]).first
                let result = try await product!.purchase()
                switch result {
                case .success(let verificationResult):
                    switch verificationResult {
                    case .verified(let transaction):
                        // Handle successful transaction
                        print("Purchase successful: \(transaction)")
                        await transaction.finish()
                        completionHandler(true, nil)
                    case .unverified(_, let error):
                        // Handle unverified transaction
                        print("Transaction unverified: \(error)")
                        completionHandler(false, nil)
                    }
                case .userCancelled:
                    print("User cancelled the purchase.")
                    completionHandler(false, nil)
                case .pending:
                    print("Transaction is pending.")
                    completionHandler(false, nil)
                }
            } catch {
                print("Purchase failed: \(error)")
                completionHandler(false, nil)
            }
        }
    }
    
    private var productsRequestDelegate: ProductsRequestDelegate?
    private var productsRequest: SKProductsRequest?
    
    
    func appPrice(completionHandler: @escaping (String?, (any Error)?) -> Void) {
        let request = SKProductsRequest(productIdentifiers: ["01"])
        
        let delegate = ProductsRequestDelegate { result in
            switch result {
            case .success(let products):
                guard let storeProduct = products.first else {
                    completionHandler(nil, nil)
                    return
                }
                let formatter = NumberFormatter()
                formatter.numberStyle = .currency
                formatter.locale = storeProduct.priceLocale
                let price = formatter.string(from: storeProduct.price) ?? "\(String(describing: storeProduct.price))"
                completionHandler(price, nil)
            case .failure(let error):
                completionHandler(nil, error)
            }
        }
        self.productsRequestDelegate = delegate
        self.productsRequest = request
        request.delegate = delegate
        request.start()
    }
    
    // Helper delegate to handle the products request using async/await
    private class ProductsRequestDelegate: NSObject, SKProductsRequestDelegate {
        private let completion: (Result<[SKProduct], Error>) -> Void
        
        init(completion: @escaping (Result<[SKProduct], Error>) -> Void) {
            self.completion = completion
        }
        
        func productsRequest(_ request: SKProductsRequest, didReceive response: SKProductsResponse) {
            completion(.success(response.products))
        }
        
        func request(_ request: SKRequest, didFailWithError error: Error) {
            completion(.failure(error))
        }
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
