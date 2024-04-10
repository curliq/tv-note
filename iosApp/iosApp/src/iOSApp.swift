import SwiftUI
import ComposeApp

@available(iOS 16.0, *)
@main
struct iOSApp: App {
    init() {
        IosModulesKt.startKoin()
    }
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
