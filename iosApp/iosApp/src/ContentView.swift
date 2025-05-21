import UIKit
import FirebaseCore
import SwiftUI
import ComposeApp

// Define all possible navigation routes
enum Route: Hashable {
    case search(origin: AddTrackedScreenOriginScreen)
    case details(content: DetailsViewModel.LoadContent)
    case episodes
    case reviews
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

// Navigation state container for each tab
class TabNavigationState: ObservableObject {
    @Published var path = NavigationPath()
    @Published var refreshId = UUID()

    func refresh() {
        refreshId = UUID()
    }
}

struct ContentView: View {

    @Environment(\.openURL) var openURL
    @Environment(\.colorScheme) var colorScheme

    // Create a navigation state for each tab
    @StateObject private var watchingNavState = TabNavigationState()
    @StateObject private var finishedNavState = TabNavigationState()
    @StateObject private var watchlistNavState = TabNavigationState()
    @StateObject private var discoverNavState = TabNavigationState()

    // Create a details view model for each tab
    let watchingDetailsVM = ViewModelsModule().detailsViewModel
    let finishedDetailsVM = ViewModelsModule().detailsViewModel
    let watchlistDetailsVM = ViewModelsModule().detailsViewModel
    let discoverDetailsVM = ViewModelsModule().detailsViewModel

    let watchingPersonVM = ViewModelsModule().personViewModel
    let finishedPersonVM = ViewModelsModule().personViewModel
    let watchlistPersonVM = ViewModelsModule().personViewModel
    let discoverPersonVM = ViewModelsModule().personViewModel

    // Tab-specific view models
    let watchingViewModel = ViewModelsModule().watchingViewModel
    let finishedViewModel = ViewModelsModule().finishedShowsViewModel
    let watchlistedViewModel = ViewModelsModule().watchlistedShowsViewModel
    let discoverViewModel = ViewModelsModule().discoverViewModel
    let settingsViewModel = ViewModelsModule().settingsViewModel
    let addTrackedViewModel = ViewModelsModule().addTrackedViewModel


    @State private var accountSelection: String? = nil
    @State private var showAccount: Bool = false

    var body: some View {
        TabView {
            // WATCHING TAB
            watchingTab
                .tabItem {
                    Label("Watching", systemImage: "play.tv.fill")
                }

            // FINISHED TAB
            finishedTab
                .tabItem {
                    Label("Finished", systemImage: "flag.checkered")
                }

            // WATCHLIST TAB
            watchlistTab
                .tabItem {
                    Label("Watchlist", systemImage: "star.square")
                }

            // DISCOVER TAB
            discoverTab
                .tabItem {
                    Label("Discover", systemImage: "safari")
                }
        }
        .nestedBackgroundColor(isDarkTheme: colorScheme == .dark)
        .onAppear {
            configureAppearance()
            configurePushNotifications()
        }
    }

    // MARK: - Tab Views

    private var watchingTab: some View {
        NavigationStack(path: $watchingNavState.path) {
            VStack {
                WatchingScreen(
                    navigate: createWatchingNavHandler(),
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
                .id(watchingNavState.refreshId)
                .onAppear {
                    watchingNavState.refresh()
                }
            }
            .sheet(isPresented: $showAccount) {
                accountSheet
            }
            .navigationDestination(for: Route.self) { route in
                handleRouteNavigation(
                    route: route,
                    navState: watchingNavState,
                    detailsViewModel: watchingDetailsVM,
                    personViewModel: watchingPersonVM
                )
            }
            .navigationViewStyle(StackNavigationViewStyle())
        }
    }

    private var finishedTab: some View {
        NavigationStack(path: $finishedNavState.path) {
            VStack {
                FinishedScreen(
                    finishedViewModel: finishedViewModel,
                    nav: createFinishedNavHandler()
                )
                .styleToolbar(title: "Finished Watching")
                .id(finishedNavState.refreshId)
                .onAppear {
                    finishedNavState.refresh()
                }
            }
            .navigationDestination(for: Route.self) { route in
                handleRouteNavigation(
                    route: route,
                    navState: finishedNavState,
                    detailsViewModel: finishedDetailsVM,
                    personViewModel: finishedPersonVM
                )
            }
            .navigationViewStyle(StackNavigationViewStyle())
        }
    }

    private var watchlistTab: some View {
        NavigationStack(path: $watchlistNavState.path) {
            VStack {
                WatchlistScreen(
                    watchlistViewModel: watchlistedViewModel,
                    nav: createWatchlistNavHandler()
                )
                .styleToolbar(title: "Watchlist")
                .id(watchlistNavState.refreshId)
                .onAppear {
                    watchlistNavState.refresh()
                }
            }
            .navigationDestination(for: Route.self) { route in
                handleRouteNavigation(
                    route: route,
                    navState: watchlistNavState,
                    detailsViewModel: watchlistDetailsVM,
                    personViewModel: watchlistPersonVM
                )
            }
            .navigationViewStyle(StackNavigationViewStyle())
        }
    }

    private var discoverTab: some View {
        NavigationStack(path: $discoverNavState.path) {
            VStack {
                DiscoverScreen(
                    discoverViewModel: discoverViewModel,
                    nav: createDiscoverNavHandler()
                )
                .styleToolbar(title: "Discover")
                .id(discoverNavState.refreshId)
                .onAppear {
                    discoverNavState.refresh()
                }
            }
            .navigationDestination(for: Route.self) { route in
                handleRouteNavigation(
                    route: route,
                    navState: discoverNavState,
                    detailsViewModel: discoverDetailsVM,
                    personViewModel: discoverPersonVM
                )
            }
            .navigationViewStyle(StackNavigationViewStyle())
        }
    }

    // MARK: - Navigation Handlers

    private func createWatchingNavHandler() -> (WatchingScreenNavAction) -> Void {
        return { navAction in
            switch navAction {
            case _ as WatchingScreenNavAction.GoAddShow:
                watchingNavState.path.append(Route.search(origin: AddTrackedScreenOriginScreen.watching))
            case let action as WatchingScreenNavAction.GoShowDetails:
                watchingNavState.path.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: true)))
            default: break
            }
        }
    }

    private func createFinishedNavHandler() -> (FinishedScreenNavAction) -> Void {
        return { action in
            switch action {
            case _ as FinishedScreenNavAction.GoAddShow:
                finishedNavState.path.append(Route.search(origin: AddTrackedScreenOriginScreen.finished))
            case let action as FinishedScreenNavAction.GoShowDetails:
                finishedNavState.path.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: action.isTvShow)))
            default: break
            }
        }
    }

    private func createWatchlistNavHandler() -> (WatchlistScreenNavAction) -> Void {
        return { action in
            switch action {
            case _ as WatchlistScreenNavAction.GoAddShow:
                watchlistNavState.path.append(Route.search(origin: AddTrackedScreenOriginScreen.watchlist))
            case let action as WatchlistScreenNavAction.GoShowDetails:
                watchlistNavState.path.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: action.isTvShow)))
            default: break
            }
        }
    }

    private func createDiscoverNavHandler() -> (DiscoverScreenNavActions) -> Void {
        return { action in
            switch action {
            case _ as DiscoverScreenNavActions.GoAddShow:
                discoverNavState.path.append(Route.search(origin: AddTrackedScreenOriginScreen.discover))
            case _ as DiscoverScreenNavActions.GoNewRelease:
                discoverNavState.path.append(Route.newReleases)
            case _ as DiscoverScreenNavActions.GoRecommendations:
                discoverNavState.path.append(Route.recommended)
            case let action as DiscoverScreenNavActions.GoShowDetails:
                discoverNavState.path.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: action.isTvShow)))
            case _ as DiscoverScreenNavActions.GoTrending:
                discoverNavState.path.append(Route.trending)
            default: break
            }
        }
    }

    // MARK: - Route Navigation Handler

    private func handleRouteNavigation(
        route: Route,
        navState: TabNavigationState,
        detailsViewModel: DetailsViewModel,
        personViewModel: PersonViewModel
    ) -> some View {
        // Create navigation handlers for the route
        let addTrackedNav = createAddTrackedNavHandler(navState: navState)
        let detailsNav = createDetailsNavHandler(navState: navState)
        let personNav = createPersonNavHandler(navState: navState)
        let recommendedNav = createRecommendedNavHandler(navState: navState)

        // Handle the route
        let contentView: AnyView

        switch route {
        case .search(let origin):
            contentView = SearchScreen(
                addTrackedViewModel: addTrackedViewModel,
                origin: origin,
                nav: addTrackedNav
            ).eraseToAnyView()

        case .details(let content):
            contentView = ShowDetailsScreen(
                detailsViewModel: detailsViewModel,
                content: content,
                nav: detailsNav
            )
            .id(UUID()) // Always refresh details view
            .eraseToAnyView()

        case .episodes:
            contentView = DetailsEpisodesSheet(detailsViewModel: detailsViewModel).eraseToAnyView()

        case .reviews:
            contentView = DetailsReviewsSheet(detailsViewModel: detailsViewModel).eraseToAnyView()

        case .cast:
            contentView = DetailsCastCrewSheet(detailsViewModel: detailsViewModel, nav: detailsNav).eraseToAnyView()

        case .person(let personId):
            contentView = PersonDetails(vm: personViewModel, personId: personId, nav: personNav).eraseToAnyView()

        case .media:
            contentView = DetailsMediaSheet(detailsViewModel: detailsViewModel, nav: detailsNav).eraseToAnyView()

        case .filmCollection:
            contentView = DetailsFilmCollectionSheet(detailsViewModel: detailsViewModel, nav: detailsNav).eraseToAnyView()

        case .personShows:
            contentView = PersonShowsDetails(vm: personViewModel, nav: personNav).eraseToAnyView()

        case .personMovies:
            contentView = PersonMoviesDetails(vm: personViewModel, nav: personNav).eraseToAnyView()

        case .personPhotos:
            contentView = PersonPhotosDetails(vm: personViewModel).eraseToAnyView()

        case .newReleases:
            contentView = NewReleasesScreen(
                discoverViewModel: discoverViewModel,
                nav: createDiscoverNavHandler()
            ).eraseToAnyView()

        case .recommended:
            contentView = RecommendedScreen(discoverViewModel: discoverViewModel, nav: recommendedNav).eraseToAnyView()

        case .trending:
            contentView = TrendingScreen(
                discoverViewModel: discoverViewModel,
                nav: createDiscoverNavHandler()
            ).eraseToAnyView()

        default:
            contentView = Text("Error: Unknown route").eraseToAnyView()
        }

        // Add optional share button for details pages
        return contentView.toolbar {
            if case .details = route {
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

    // MARK: - Sub-navigation Handlers

    private func createAddTrackedNavHandler(navState: TabNavigationState) -> (AddTrackedScreenNavAction) -> Void {
        return { action in
            switch action {
            case let action as AddTrackedScreenNavAction.GoContentDetails:
                navState.path.append(Route.details(content: DetailsViewModel.LoadContent(
                    tmdbId: action.showTmdbId,
                    isTvShow: action.isTvShow
                )))
            case let action as AddTrackedScreenNavAction.GoPersonDetails:
                navState.path.append(Route.person(personId: action.personTmdbId))
            default: break
            }
        }
    }

    private func createDetailsNavHandler(navState: TabNavigationState) -> (DetailsScreenNavAction) -> Void {
        return { navAction in
            switch navAction {
            case let action as DetailsScreenNavAction.GoYoutube:
                openURL(URL(string: action.webUrl)!)
            case _ as DetailsScreenNavAction.GoAllEpisodes:
                navState.path.append(Route.episodes)
            case _ as DetailsScreenNavAction.GoReviews:
                navState.path.append(Route.reviews)
            case _ as DetailsScreenNavAction.GoMedia:
                navState.path.append(Route.media)
            case _ as DetailsScreenNavAction.GoCastAndCrew:
                navState.path.append(Route.cast)
            case _ as DetailsScreenNavAction.GoFilmCollection:
                navState.path.append(Route.filmCollection)
            case let action as DetailsScreenNavAction.GoCastAndCrewDetails:
                navState.path.append(Route.person(personId: action.personTmdbId))
            case let action as DetailsScreenNavAction.GoContentDetails:
                navState.path.append(Route.details(content: DetailsViewModel.LoadContent(
                    tmdbId: action.tmdbId,
                    isTvShow: action.isTvShow
                )))
            case let action as DetailsScreenNavAction.GoWebsite:
                openURL(URL(string: action.url)!)
            default: break
            }
        }
    }

    private func createPersonNavHandler(navState: TabNavigationState) -> (PersonScreenNavAction) -> Void {
        return { action in
            switch action {
            case let action as PersonScreenNavAction.GoShowDetails:
                navState.path.append(Route.details(content: DetailsViewModel.LoadContent(
                    tmdbId: action.tmdbShowId,
                    isTvShow: action.isTvShow
                )))
            case _ as PersonScreenNavAction.GoAllMovies:
                navState.path.append(Route.personMovies)
            case _ as PersonScreenNavAction.GoAllPhotos:
                navState.path.append(Route.personPhotos)
            case _ as PersonScreenNavAction.GoAllShows:
                navState.path.append(Route.personShows)
            case let action as PersonScreenNavAction.GoInstagram:
                openURL(URL(string: action.url)!)
            default: break
            }
        }
    }

    private func createRecommendedNavHandler(navState: TabNavigationState) -> (RecommendedScreenNavActions) -> Void {
        return { action in
            switch action {
            case let action as RecommendedScreenNavActions.GoShowDetails:
                navState.path.append(Route.details(content: DetailsViewModel.LoadContent(
                    tmdbId: action.tmdbShowId,
                    isTvShow: action.isTvShow
                )))
            default: break
            }
        }
    }

    // MARK: - Account Sheet View

    private var accountSheet: some View {
        NavigationView {
            VStack {
                let navSettings: (SettingsScreenNavAction) -> Void = { navAction in
                    switch navAction {
                    case _ as SettingsScreenNavAction.GoLogin:
                        accountSelection = "login"
                    case _ as SettingsScreenNavAction.GoSignup:
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
                    default: break
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

                // Auth navigation links
                NavigationLink(
                    destination: LoginScreen(vm: ViewModelsModule().loginViewModel, nav: navBack)
                        .nestedBackgroundColor(isDarkTheme: colorScheme == .dark),
                    tag: "login",
                    selection: $accountSelection
                ) { EmptyView() }

                NavigationLink(
                    destination: SignupScreen(vm: ViewModelsModule().signupViewModel, nav: navBack)
                        .nestedBackgroundColor(isDarkTheme: colorScheme == .dark),
                    tag: "signup",
                    selection: $accountSelection
                ) { EmptyView() }
            }
        }
        .sheetBackgroundColor(isDarkTheme: colorScheme == .dark)
    }

    // MARK: - Utility Methods

    private func configureAppearance() {
        // Configure tab bar appearance
        let standardAppearance = UITabBarAppearance()
        standardAppearance.configureWithTransparentBackground()
        standardAppearance.backgroundColor = UIColor.systemGray6
        standardAppearance.stackedLayoutAppearance.normal.titleTextAttributes = [
            .font: UIFont(name: "IBMPlexSans-Regular", size: 10)!
        ]
        UITabBar.appearance().standardAppearance = standardAppearance

        // Configure navigation bar appearance
        let navbarAppearance = UINavigationBarAppearance()
        navbarAppearance.backButtonAppearance.normal.titleTextAttributes = [
            .font: UIFont(name: "IBMPlexSans-Regular", size: 17)!
        ]
        UINavigationBar.appearance().standardAppearance = navbarAppearance

        // Configure title text attributes
        UINavigationBar.appearance().largeTitleTextAttributes = [
            .font: UIFont(name: "IBMPlexSans-Bold", size: 34)!
        ]
        UINavigationBar.appearance().titleTextAttributes = [
            .font: UIFont(name: "IBMPlexSans-Semibold", size: 17)!
        ]
    }

    private func configurePushNotifications() {
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

// MARK: - Helper Views

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
                AddTrackedScreen(
                    addTrackedViewModel: addTrackedViewModel,
                    origin: origin,
                    nav: nav
                )
            }
        }
        .navigationTitle(title())
        .navigationBarTitleDisplayMode(.inline)
        .eraseToAnyView()
    }
}

// MARK: - Helpers

struct ShareItemProvider: Transferable {
    let vm: DetailsViewModel

    init(vm: DetailsViewModel) {
        self.vm = vm
    }

    func generateReport() -> String {
        print("Generating...")
        return vm.getShareLink()
    }

    static var transferRepresentation: some TransferRepresentation {
        ProxyRepresentation { report in
            URL(string: report.generateReport())!
        }
    }
}

// MARK: - View Extensions

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
                        .font(.custom("IBMPlexSans-Bold", size: 22))
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

// MARK: - UIKit Extensions

extension UINavigationController: UIGestureRecognizerDelegate {
    override open func viewDidLoad() {
        super.viewDidLoad()
        interactivePopGestureRecognizer?.delegate = self
    }

    public func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
        return viewControllers.count > 1
    }
}
