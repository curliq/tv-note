import Foundation
import ComposeApp
import StoreKit

class IosAppPriceProvider: AppPriceProvider {

    let purchaseId = "com.free.tvtracker.BestTvTracker.accesslifetime"
    let subId = "com.free.tvtracker.BestTvTracker.accesssub"

    func restorePurchase() async throws -> KotlinBoolean {
        // Get the most recent transaction for each product
        let verificationResult = await Transaction.latest(for: purchaseId)
        if case .verified(let transaction) = verificationResult {
            print("Restored: \(transaction.productID)")
            return true
        }

        let subVerificationResult = await Transaction.latest(for: subId)
        if case .verified(let transaction) = subVerificationResult {
            print("Restored: \(transaction.productID)")
            return true
        }

        return false
    }

    func buyApp(completionHandler: @escaping (KotlinBoolean?, (any Error)?) -> Void) {
        Task {
            do {
                guard let product = try await Product.products(for: [purchaseId]).first else {
                    completionHandler(false, nil)
                    return
                }
                let result = try await product.purchase()
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

    func appPrice(completionHandler: @escaping (String?, (any Error)?) -> Void) {
        Task {
            do {
                guard let product = try await Product.products(for: [purchaseId]).first else {
                    completionHandler(nil, nil)
                    return
                }

                let formatter = NumberFormatter()
                formatter.numberStyle = .currency
                formatter.locale = product.priceFormatStyle.locale
                let price = formatter.string(from: product.price as NSDecimalNumber) ?? "\(product.price)"
                completionHandler(price, nil)
            } catch {
                print("Failed to fetch product price: \(error)")
                completionHandler(nil, error)
            }
        }
    }

    func appSubPrice(completionHandler: @escaping (String?, (any Error)?) -> Void) {
        Task {
            do {
                print("Fetching subscription price")
                guard let product = try await Product.products(for: [subId]).first else {
                    print("No subscription product found")
                    completionHandler(nil, nil)
                    return
                }

                let formatter = NumberFormatter()
                formatter.numberStyle = .currency
                formatter.locale = product.priceFormatStyle.locale
                let price = formatter.string(from: product.price as NSDecimalNumber) ?? "\(product.price)"
                print("Subscription price: \(price)")
                completionHandler(price, nil)
            } catch {
                print("Failed to fetch subscription price: \(error)")
                completionHandler(nil, error)
            }
        }
    }

    func subscribe(completionHandler: @escaping (KotlinBoolean?, (any Error)?) -> Void) {
        Task {
            do {
                guard let product = try await Product.products(for: [subId]).first else {
                    completionHandler(false, nil)
                    return
                }
                let result = try await product.purchase()
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
}
