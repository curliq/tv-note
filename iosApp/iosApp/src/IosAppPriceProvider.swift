import Foundation
import ComposeApp
import StoreKit

class IosAppPriceProvider: AppPriceProvider {
    let purchaseId = "com.free.tvtracker.BestTvTracker.accesslifetime"
    let subId = "com.free.tvtracker.BestTvTracker.accesssub"
    
    func restorePurchase() async throws -> KotlinBoolean {
        for await item in Transaction.currentEntitlements {
            if case let .verified(transaction) = item {
                let productID = transaction.productID
                print("Restored: \(productID)")
                if (productID == purchaseId || productID == subId) {
                    return true
                }
            }
        }
        return false
    }
    
    class ProductFetcher: NSObject, SKProductsRequestDelegate {
        var products: [SKProduct] = []
        
        func fetchProducts(productIdentifiers: Set<String>) {
            let request = SKProductsRequest(productIdentifiers: productIdentifiers)
            request.delegate = self
            request.start()
        }
        
        func productsRequest(_ request: SKProductsRequest, didReceive response: SKProductsResponse) {
            products = response.products
            // Handle the received products (e.g., display them in your UI)
            for product in products {
                print("Product found: \(product.localizedTitle) - \(product.price)")
            }
        }
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
    
    private var productsRequestDelegate: ProductsRequestDelegate?
    private var productsRequest: SKProductsRequest?
    
    func appPrice(completionHandler: @escaping (String?, (any Error)?) -> Void) {
        let request = SKProductsRequest(productIdentifiers: [purchaseId])
        
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
    
    func appSubPrice(completionHandler: @escaping (String?, (any Error)?) -> Void) {
        let request = SKProductsRequest(productIdentifiers: [subId])
        
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
