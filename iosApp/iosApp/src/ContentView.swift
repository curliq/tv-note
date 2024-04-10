import UIKit
import SwiftUI
import ComposeApp

struct ContentView: View {
    @State private var selection: String? = nil
    @State private var showCategorySelector: Bool = false
    @State private var selectedShowId: Int32 = -1
    let vm1 = ViewModelsModule().watchingViewModel
    let vm2 = ViewModelsModule().addTrackedViewModel
    
    var body: some View {
        TabView {
            NavigationView {
                VStack {
                    let navActions: (NavAction) -> Void = { navAction in
                        switch navAction {
                        case _ as NavAction.GoAddShow:
                            selection = "addShow"
                            //                                showCategorySelector.toggle()
                        case let action as NavAction.GoShowDetails:
                            selectedShowId = action.showId
                            selection = "showDetails"
                        default: break
                        }
                    }
                    NavigationLink(destination: ShowDetailsScreen(detailsViewModel: ViewModelsModule().detailsViewModel, showId: selectedShowId), tag: "showDetails", selection: $selection) { EmptyView() }
                    NavigationLink(destination: AddTrackedScreen(addTrackedViewModel: vm2).hideToolbar(), tag: "addShow", selection: $selection) { EmptyView() }
                    ScrollView {
                        WatchingScreen(
                            navigate: navActions,
                            watchingViewModel: vm1
                        )
                        .frame(width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height)
                    }
                    .ignoresSafeArea(.keyboard)
                    .navigationTitle("Watching")
                    .toolbar {
//                        ToolbarItemGroup(placement: .primaryAction) {
//                            Button {
//                                navActions(NavAction.GoAddShow())
//                            } label: {
//                                Image(systemName: "plus.circle")
//                            }
//                        }
                    }
                }
            }
            .sheet(isPresented: $showCategorySelector) {
                //                AddTrackedScreen()
            }
            .navigationViewStyle(StackNavigationViewStyle())
            .tabItem {
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
