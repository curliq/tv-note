import UIKit
import FirebaseCore
import SwiftUI
import ComposeApp

enum Route: Hashable {
    case search(origin: AddTrackedScreenOriginScreen)
    case details(content: DetailsViewModel.LoadContent)
    case episodes
    case cast
    case media
    case filmCollection
    case person(personId: Int32)
    case personShows
    case personMovies
    case personPhotos
    case newReleases
    case recommended
    case trending
}

struct ContentView: View {
    
    @Environment(\.openURL) var openURL
    @Environment(\.colorScheme) var colorScheme
    
    @State private var accountSelection: String? = nil
    let watchingViewModel = ViewModelsModule().watchingViewModel
    let finishedViewModel = ViewModelsModule().finishedShowsViewModel
    let watchlistedViewModel = ViewModelsModule().watchlistedShowsViewModel
    let discoverViewModel = ViewModelsModule().discoverViewModel
    let settingsViewModel = ViewModelsModule().settingsViewModel
    let detailsViewModel = ViewModelsModule().detailsViewModel
    let personViewModel = ViewModelsModule().personViewModel
    let addTrackedViewModel = ViewModelsModule().addTrackedViewModel
    @State private var path1 = NavigationPath()
    @State private var path2 = NavigationPath()
    @State private var path3 = NavigationPath()
    @State private var path4 = NavigationPath()
    @State var showAccount: Bool = false
    
    func discoverNav(path: Binding<NavigationPath>) -> (DiscoverScreenNavActions) -> Void {
        return { action in
            switch action {
            case _ as DiscoverScreenNavActions.GoAddShow:
                path.wrappedValue.append(Route.search(origin: AddTrackedScreenOriginScreen.discover))
            case _ as DiscoverScreenNavActions.GoNewRelease:
                path.wrappedValue.append(Route.newReleases)
            case _ as DiscoverScreenNavActions.GoRecommendations:
                path.wrappedValue.append(Route.recommended)
            case let action as DiscoverScreenNavActions.GoShowDetails:
                path.wrappedValue.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: action.isTvShow)))
            case _ as DiscoverScreenNavActions.GoTrending:
                path.wrappedValue.append(Route.trending)
            default:
                break
            }
        }
    }
    
    var body: some View {
        TabView {
            NavigationStack(path: $path1) {
                let detailsViewModel = ViewModelsModule().detailsViewModel
                VStack {
                    let navActions: (WatchingScreenNavAction) -> Void = { navAction in
                        switch navAction {
                        case _ as WatchingScreenNavAction.GoAddShow:
                            path1.append(Route.search(origin: AddTrackedScreenOriginScreen.watching))
                        case let action as WatchingScreenNavAction.GoShowDetails:
                            path1.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: true)))
                        default: break
                        }
                    }
                    
                    WatchingScreen(
                        navigate: navActions,
                        watchingViewModel: watchingViewModel
                    )
                    .styleToolbar(title: "Currently Watching")
                    .toolbar {
                        ToolbarItemGroup(placement: .primaryAction) {
                            Button {
                                showAccount = true
                            } label: {
                                Image(systemName: "gear")
                            }
                        }
                    }
                }
                .sheet(isPresented: $showAccount) {
                    NavigationView {
                        VStack {
                            let navSettings: (SettingsScreenNavAction) -> Void = { navAction in
                                switch navAction {
                                case let action as SettingsScreenNavAction.GoLogin:
                                    accountSelection = "login"
                                case let action as SettingsScreenNavAction.GoSignup:
                                    accountSelection = "signup"
                                case let action as SettingsScreenNavAction.EmailSupport:
                                    if let url = URL(string: "mailto:\(action.email)") {
                                        if #available(iOS 10.0, *) {
                                            UIApplication.shared.open(url)
                                        } else {
                                            UIApplication.shared.openURL(url)
                                        }
                                    }
                                case let action as SettingsScreenNavAction.GoBrowser:
                                    openURL(URL(string: action.url)!)
                                default:
                                    break
                                }
                            }
                            SettingsScreen(vm: settingsViewModel, nav: navSettings)
                                .navigationTitle("Settings")
                                .navigationBarTitleDisplayMode(.inline)
                                .toolbar {
                                    ToolbarItem(placement: .navigationBarTrailing) {
                                        Button("Done", role: .cancel) {
                                            showAccount = false
                                        }
                                        .font(Font.custom("IBMPlexSans-Bold", size: 17))
                                    }
                                }
                            let navBack: (Any) -> Void = { _ in
                                accountSelection = ""
                                showAccount = false
                            }
                            NavigationLink(
                                destination: LoginScreen(vm: ViewModelsModule().loginViewModel, nav: navBack).nestedBackgroundColor(isDarkTheme: colorScheme == .dark),
                                tag: "login",
                                selection: $accountSelection
                            ) { EmptyView() }
                            NavigationLink(
                                destination: SignupScreen(vm: ViewModelsModule().signupViewModel, nav: navBack).nestedBackgroundColor(isDarkTheme: colorScheme == .dark),
                                tag: "signup",
                                selection: $accountSelection
                            ) { EmptyView() }
                        }
                    }
                    .sheetBackgroundColor(isDarkTheme: colorScheme == .dark)
                }
                .navigationDestination(for: Route.self) { route in
                    handleRouteNavigation(route: route, path: $path1, detailsViewModel: detailsViewModel)
                }
                .navigationViewStyle(StackNavigationViewStyle())
            }
            .tabItem {
                Label("Watching", systemImage: "play.tv.fill")
            }
            
            NavigationStack(path: $path2) {
                let detailsViewModel = ViewModelsModule().detailsViewModel
                VStack {
                    let finishedNav: (FinishedScreenNavAction) -> Void = { action in
                        switch action {
                        case _ as FinishedScreenNavAction.GoAddShow:
                            path2.append(Route.search(origin: AddTrackedScreenOriginScreen.finished))
                        case let action as FinishedScreenNavAction.GoShowDetails:
                            path2.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: action.isTvShow)))
                        default:
                            break
                        }
                    }
                    FinishedScreen(finishedViewModel: finishedViewModel, nav: finishedNav)
                        .styleToolbar(title: "Finished Watching")
                }
                .navigationDestination(for: Route.self) { route in
                    handleRouteNavigation(route: route, path: $path2, detailsViewModel: detailsViewModel)
                }
                .navigationViewStyle(StackNavigationViewStyle())
            }
            .tabItem {
                Label("Finished", systemImage: "flag.checkered")
            }
            
            NavigationStack(path: $path3) {
                let detailsViewModel = ViewModelsModule().detailsViewModel
                VStack {
                    let watchlistNav: (WatchlistScreenNavAction) -> Void = { action in
                        switch action {
                        case _ as WatchlistScreenNavAction.GoAddShow:
                            path3.append(Route.search(origin: AddTrackedScreenOriginScreen.watchlist))
                        case let action as WatchlistScreenNavAction.GoShowDetails:
                            path3.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: action.isTvShow)))
                        default:
                            break
                        }
                    }
                    WatchlistScreen(watchlistViewModel: watchlistedViewModel, nav: watchlistNav)
                        .styleToolbar(title: "Watchlist")
                }
                .navigationDestination(for: Route.self) { route in
                    handleRouteNavigation(route: route, path: $path3, detailsViewModel: detailsViewModel)
                }
                .navigationViewStyle(StackNavigationViewStyle())
            }.tabItem {
                Label("Watchlist", systemImage: "star.square")
            }
            
            NavigationStack(path: $path4) {
                let detailsViewModel = ViewModelsModule().detailsViewModel
                VStack {
                    DiscoverScreen(discoverViewModel: discoverViewModel, nav: discoverNav(path: $path4))
                        .styleToolbar(title: "Discover")
                }
                .navigationDestination(for: Route.self) { route in
                    handleRouteNavigation(route: route, path: $path4, detailsViewModel: detailsViewModel)
                }
                .navigationViewStyle(StackNavigationViewStyle())
            }
            .tabItem {
                Label("Discover", systemImage: "safari")
            }
        }
        .nestedBackgroundColor(isDarkTheme: colorScheme == .dark)
        .onAppear() {
            let standardAppearance = UITabBarAppearance()
            standardAppearance.configureWithTransparentBackground()
            standardAppearance.backgroundColor = UIColor.systemGray6
            // tabbar label
            standardAppearance.stackedLayoutAppearance.normal.titleTextAttributes = [
                .font: UIFont(name: "IBMPlexSans-Regular", size: 10)!
            ]
            UITabBar.appearance().standardAppearance = standardAppearance
            
            // back button
            let navbarAppearance = UINavigationBarAppearance();
            navbarAppearance.backButtonAppearance.normal.titleTextAttributes = [
                .font: UIFont(name: "IBMPlexSans-Regular", size: 17)!
            ]
            UINavigationBar.appearance().standardAppearance = navbarAppearance
            
            // large title eg Watching
            UINavigationBar.appearance().largeTitleTextAttributes = [
                .font: UIFont(name: "IBMPlexSans-Bold", size: 34)!
            ]
            
            // normal size title, eg search screen
            UINavigationBar.appearance().titleTextAttributes = [
                .font: UIFont(name: "IBMPlexSans-Semibold", size: 17)!
            ]
            
            // push permission
            
            // Firebase libraries from SPM only work for arm64
            // but not m1 simulators for undoubtedly good and valid reasons
#if (!targetEnvironment(simulator))
            FirebaseApp.configure()
            let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(
                options: authOptions,
                completionHandler: { granted, error in
                    if (granted) {
                        DispatchQueue.main.async {
                            UIApplication.shared.registerForRemoteNotifications()
                        }
                    }
                    
                }
            )
            
            
#endif
        }
    }
    
    func handleRouteNavigation(
        route: Route,
        path: Binding<NavigationPath>,
        detailsViewModel: DetailsViewModel
    ) -> some View {
        let addTrackedNav: (AddTrackedScreenNavAction) -> Void = { action in
            switch action {
            case let action as AddTrackedScreenNavAction.GoContentDetails:
                path.wrappedValue.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.showTmdbId, isTvShow: action.isTvShow)))
            case let action as AddTrackedScreenNavAction.GoPersonDetails:
                path.wrappedValue.append(Route.person(personId: action.personTmdbId))
            default:
                break
            }
        }
        let detailsNav: (DetailsScreenNavAction) -> Void = { navAction in
            switch navAction {
            case let action as DetailsScreenNavAction.GoYoutube:
                openURL(URL(string: action.webUrl)!)
            case _ as DetailsScreenNavAction.GoAllEpisodes:
                path.wrappedValue.append(Route.episodes)
            case _ as DetailsScreenNavAction.GoMedia:
                path.wrappedValue.append(Route.media)
            case _ as DetailsScreenNavAction.GoCastAndCrew:
                path.wrappedValue.append(Route.cast)
            case _ as DetailsScreenNavAction.GoFilmCollection:
                path.wrappedValue.append(Route.filmCollection)
            case let action as DetailsScreenNavAction.GoCastAndCrewDetails:
                path.wrappedValue.append(Route.person(personId: action.personTmdbId))
            case let action as DetailsScreenNavAction.GoContentDetails:
                path.wrappedValue.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbId, isTvShow: action.isTvShow)))
            case let action as DetailsScreenNavAction.GoWebsite:
                openURL(URL(string: action.url)!)
            default:
                break
            }
        }
        let personNav: (PersonScreenNavAction) -> Void = { action in
            switch action {
            case let action as PersonScreenNavAction.GoShowDetails:
                path.wrappedValue.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: action.isTvShow)))
            case _ as PersonScreenNavAction.GoAllMovies:
                path.wrappedValue.append(Route.personMovies)
            case _ as PersonScreenNavAction.GoAllPhotos:
                path.wrappedValue.append(Route.personPhotos)
            case _ as PersonScreenNavAction.GoAllShows:
                path.wrappedValue.append(Route.personShows)
            case let action as PersonScreenNavAction.GoInstagram:
                openURL(URL(string: action.url)!)
            default:
                break
            }
        }
        let recommendedNav: (RecommendedScreenNavActions) -> Void = { action in
            switch action {
            case let action as RecommendedScreenNavActions.GoShowDetails:
                path.wrappedValue.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: action.isTvShow)))
            default:
                break
            }
        }
        
        let aa = switch route {
        case .search(let origin):
            SearchScreen(addTrackedViewModel: addTrackedViewModel, origin: origin, nav: addTrackedNav).eraseToAnyView()
        case .details(let content):
            ShowDetailsScreen(detailsViewModel: detailsViewModel, content: content, nav: detailsNav).eraseToAnyView()
        case .episodes:
            DetailsEpisodesSheet(detailsViewModel: detailsViewModel).eraseToAnyView()
        case .cast:
            DetailsCastCrewSheet(detailsViewModel: detailsViewModel, nav: detailsNav).eraseToAnyView()
        case .person(let personId):
            PersonDetails(vm: personViewModel, personId: personId, nav: personNav).eraseToAnyView()
        case .media:
            DetailsMediaSheet(detailsViewModel: detailsViewModel, nav: detailsNav).eraseToAnyView()
        case .filmCollection:
            DetailsFilmCollectionSheet(detailsViewModel: detailsViewModel, nav: detailsNav).eraseToAnyView()
        case .personShows:
            PersonShowsDetails(vm: personViewModel, nav: personNav).eraseToAnyView()
        case .personMovies:
            PersonMoviesDetails(vm: personViewModel, nav: personNav).eraseToAnyView()
        case .personPhotos:
            PersonPhotosDetails(vm: personViewModel).eraseToAnyView()
        case .newReleases:
            NewReleasesScreen(discoverViewModel: discoverViewModel, nav: discoverNav(path: $path4)).eraseToAnyView()
        case .recommended:
            RecommendedScreen(discoverViewModel: discoverViewModel, nav: recommendedNav).eraseToAnyView()
        case .trending:
            TrendingScreen(discoverViewModel: discoverViewModel, nav: discoverNav(path: $path4)).eraseToAnyView()
        default:
            Text("error").eraseToAnyView()
        }
        return aa.toolbar {
            if case .details(let content) = route {
                ToolbarItem(placement: .navigationBarTrailing) {
                    ShareLink(
                        item: ShareItemProvider(vm: detailsViewModel),
                        preview: SharePreview("Sharing link...")
                    ) {
                        Image(systemName: "square.and.arrow.up")
                    }
                }
            }
        }
    }
    
}

struct ShareItemProvider: Transferable {
    
    let vm: DetailsViewModel
    
    init(vm: DetailsViewModel) {
        self.vm = vm
    }
    
    func generateReport() -> String {
        print("Generating...")
        // do some work...
        return vm.getShareLink() // Assuming getShareLink() returns a String
    }
    
    // Proper Transferable conformance with ProxyRepresentation
    static var transferRepresentation: some TransferRepresentation {
        ProxyRepresentation { report in
            URL(string: report.generateReport())!
        }
    }
}

struct SearchScreen: View {
    
    let addTrackedViewModel: AddTrackedViewModel
    let origin: AddTrackedScreenOriginScreen
    let nav: (AddTrackedScreenNavAction) -> Void
    
    private func title() -> String {
        switch origin {
        case AddTrackedScreenOriginScreen.watching:
            return "Add to currently watching"
        case AddTrackedScreenOriginScreen.finished:
            return "Add to finished watching"
        case AddTrackedScreenOriginScreen.watchlist:
            return "Add to watchlist"
        case AddTrackedScreenOriginScreen.discover:
            return "Search"
        default:
            return ""
        }
    }
    
    var body: some View {
        NavigationView {
            VStack {
                AddTrackedScreen(addTrackedViewModel: addTrackedViewModel, origin: origin, nav: nav)
            }
        }
        .navigationTitle(title())
        .navigationBarTitleDisplayMode(.inline)
        .eraseToAnyView()
    }
}

extension View {
    func eraseToAnyView() -> AnyView {
        AnyView(self)
    }
    
    func hideToolbar() -> some View {
        if #available(iOS 16.0, *) {
            return self.toolbar(.hidden)
        } else {
            return self.navigationBarHidden(true)
        }
    }
    
    func styleToolbar(title: String) -> some View {
        return self.navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Text(title)
                        .font(.custom("IBMPlexSans-Bold", size: 22)) //title2
                }
            }
    }
    
    func sheetBackgroundColor(isDarkTheme: Bool) -> some View {
        if #available(iOS 16.4, *) {
            return self.presentationBackground(isDarkTheme ? .black : .white)
        } else {
            return self
        }
    }
    
    func nestedBackgroundColor(isDarkTheme: Bool) -> some View {
        if #available(iOS 16.4, *) {
            let color = isDarkTheme ? Color.black : Color.white
            return self.background(color.ignoresSafeArea())
        } else {
            return self
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
