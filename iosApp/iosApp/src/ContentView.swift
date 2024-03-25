import UIKit
import SwiftUI

struct ContentView: View {
    @State private var selection: String? = nil
    let content: some View = ShowDetailsScreen().hideToolbar()
    var body: some View {
        TabView {
            NavigationView {
                VStack {
                    NavigationLink(destination: content, tag: "A", selection: $selection) { EmptyView() }
                    WatchingScreen(v2: {selection = "A"}).ignoresSafeArea(.keyboard)
                }}.tabItem {
                    Label("Watching", systemImage: "play.tv.fill")
                }
            FinishedScreen()
                .tabItem {
                    Label("Finished", systemImage: "flag.checkered")
                }
            WatchlistScreen()
                .tabItem {
                    Label("Watchlist", systemImage: "star.square")
                }
            DiscoverScreen()
                .tabItem {
                    Label("Discover", systemImage: "safari")
                }
            SettingsScreen()
                .tabItem {
                    Label("Settings", systemImage: "gear")
                }
        }
    }
}

extension View {
    func hideToolbar() -> some View {
        if #available(iOS 16.0, *) {
            return self.toolbar(.hidden)
        } else {
            return self.navigationBarHidden(true)
        }
    }
}

extension UINavigationController: UIGestureRecognizerDelegate {
    override open func viewDidLoad() {
        super.viewDidLoad()
        interactivePopGestureRecognizer?.delegate = self
    }
    
    public func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
        return viewControllers.count > 1
    }
}
