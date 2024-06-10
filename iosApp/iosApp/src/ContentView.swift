import UIKit
import SwiftUI
import ComposeApp

struct ContentView: View {
    @State private var selection: String? = nil
    @State private var showCategorySelector: Bool = false
    @State private var selectedShowId: Int32 = -1
    let vm1 = ViewModelsModule().watchingViewModel
    let vm2 = ViewModelsModule().addTrackedViewModel
    let finishedViewModel = ViewModelsModule().finishedShowsViewModel
    let watchlistedViewModel = ViewModelsModule().watchlistedShowsViewModel
    let discoverViewModel = ViewModelsModule().discoverViewModel

    var body: some View {
        TabView {
            NavigationView {
                VStack {
                    let navActions: (WatchingScreenNavAction) -> Void = { navAction in
                        switch navAction {
                        case _ as WatchingScreenNavAction.GoAddShow:
                            selection = "addShow"
                            //                                showCategorySelector.toggle()
                        case let action as WatchingScreenNavAction.GoShowDetails:
                            selectedShowId = action.tmdbShowId
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
                        .navigationTitle("helo")
//                        .frame(width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height * 2)
                    }
                    .ignoresSafeArea(.keyboard)
//                    .navigationTitle("Watching")
//                    .toolbar {
//                        ToolbarItemGroup(placement: .primaryAction) {
//                            Button {
//                                navActions(NavAction.GoAddShow())
//                            } label: {
//                                Image(systemName: "plus.circle")
//                            }
//                        }
//                    }
                }
            }
            .sheet(isPresented: $showCategorySelector) {
                //                AddTrackedScreen()
            }
            .navigationViewStyle(StackNavigationViewStyle())
            .tabItem {
                Label("Watching", systemImage: "play.tv.fill")
            }
            .navigationTitle("Watching")
            
            NavigationView {
                FinishedScreen(finishedViewModel: finishedViewModel)
                    .navigationTitle("finishh")
//                Text("hey..")
            }
                .tabItem {
                    Label("Finished", systemImage: "flag.checkered")
                }
                .navigationTitle("Finished watching")
            
            WatchlistScreen(watchlistViewModel: watchlistedViewModel)
                .tabItem {
                    Label("Watchlist", systemImage: "star.square")
                }
                .navigationViewStyle(StackNavigationViewStyle())
                .navigationTitle("Watchlist")

            DiscoverScreen(discoverViewModel: discoverViewModel)

                    .tabItem {
                        Label("Discover", systemImage: "safari")
                    }
                    .ignoresSafeArea(.keyboard)
                    .edgesIgnoringSafeArea(.top)
            
            
            SettingsScreen()
                .tabItem {
                    Label("Settings", systemImage: "gear")
                }
        }
        .onAppear() {
            let standardAppearance = UITabBarAppearance()
//            standardAppearance.backgroundImage = UIImage()
//            standardAppearance.shadowColor = UIColor(Color.black)
            standardAppearance.configureWithTransparentBackground()
            standardAppearance.backgroundColor = UIColor.systemGray6
            UITabBar.appearance().standardAppearance = standardAppearance
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
