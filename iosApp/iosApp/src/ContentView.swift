import UIKit
import SwiftUI
import ComposeApp

// Define your navigation paths
enum Route: Hashable {
    // show/movie
    case details(content: DetailsViewModel.LoadContent)
    case episodes
    case cast
    case media
    case filmCollection
    case person(personId: Int32)
    case personShows
    case personMovies
    case personPhotos

    // discover
    case newReleases
    case recommended
    case trending
}

struct ContentView: View {
    
    @Environment(\.openURL) var openURL
    
    @State private var selection: String? = nil
    @State private var bottomSheetSelection: String = ""
    @State private var selectedShowId: DetailsViewModel.LoadContent = DetailsViewModel.LoadContent(tmdbId: -1, isTvShow: true)
    let vm1 = ViewModelsModule().watchingViewModel
    let vm2 = ViewModelsModule().addTrackedViewModel
    let finishedViewModel = ViewModelsModule().finishedShowsViewModel
    let watchlistedViewModel = ViewModelsModule().watchlistedShowsViewModel
    let discoverViewModel = ViewModelsModule().discoverViewModel
    let settingsViewModel = ViewModelsModule().settingsViewModel
    let detailsViewModel = ViewModelsModule().detailsViewModel
    let personViewModel = ViewModelsModule().personViewModel
    @State var detailsSelection: String? = ""
    @State var personId: Int32 = 0
    @State private var path1 = NavigationPath()
    @State private var path2 = NavigationPath()
    @State var showAccount: Bool = false

    func discoverNav(path: Binding<NavigationPath>) -> (DiscoverScreenNavActions) -> Void {
        return { action in
            switch action {
            case _ as DiscoverScreenNavActions.GoAddShow:
                // If you need to handle `GoAddShow`, uncomment the code below:
                // path.wrappedValue.append(Route.addShow)
                break
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
                VStack {
                    let navActions: (WatchingScreenNavAction) -> Void = { navAction in
                        switch navAction {
                        case _ as WatchingScreenNavAction.GoAddShow:
                            selection = "addShow"
                        case let action as WatchingScreenNavAction.GoShowDetails:
                            //                            selectedShowId = DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: true)
                            //                            selection = "showDetails"
                            path1.append(Route.details(content: DetailsViewModel.LoadContent(tmdbId: action.tmdbShowId, isTvShow: true)))
                        default: break
                        }
                    }
                    
                    NavigationLink(destination: AddTrackedScreen(addTrackedViewModel: vm2).hideToolbar(), tag: "addShow", selection: $selection) { EmptyView() }
                    
                    WatchingScreen(
                        navigate: navActions,
                        watchingViewModel: vm1
                    )
                    .styleToolbar(title: "Watching")
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
                                    selection = "login"
                                    break
                                case let action as SettingsScreenNavAction.GoSignup:
                                    selection = "signup"
                                    break
                                case let action as SettingsScreenNavAction.EmailSupport:
                                    if let url = URL(string: "mailto:\(action.email)") {
                                        if #available(iOS 10.0, *) {
                                            UIApplication.shared.open(url)
                                        } else {
                                            UIApplication.shared.openURL(url)
                                        }
                                    }
                                    break
                                case let action as SettingsScreenNavAction.GoBrowser:
                                    openURL(URL(string: action.url)!)
                                    break
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
                                        }.bold()
                                    }
                                }
                            NavigationLink(destination: LoginScreen(vm: ViewModelsModule().loginViewModel, nav: { _ in }), tag: "login", selection: $selection) { EmptyView() }
                            NavigationLink(destination: SignupScreen(vm: ViewModelsModule().signupViewModel, nav: { _ in }), tag: "signup", selection: $selection) { EmptyView() }
                        }
                        
                    }
                    
                }
                .navigationDestination(for: Route.self) { route in
                    handleRouteNavigation(route: route, path: $path1)
                }
                .navigationViewStyle(StackNavigationViewStyle())
            }
            .tabItem {
                Label("Watching", systemImage: "play.tv.fill")
            }
            
            NavigationView {
                FinishedScreen(finishedViewModel: finishedViewModel)
                    .navigationTitle("finishh")
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
            NavigationStack(path: $path2) {
                VStack {
                    DiscoverScreen(discoverViewModel: discoverViewModel, nav: discoverNav(path: $path2))
                        .styleToolbar(title: "Discover")
                }
                .navigationDestination(for: Route.self) { route in
                    handleRouteNavigation(route: route, path: $path2)
                }
                .navigationViewStyle(StackNavigationViewStyle())
            }
            .tabItem {
                Label("Discover", systemImage: "safari")
            }
        }
        .onAppear() {
            let standardAppearance = UITabBarAppearance()
            standardAppearance.configureWithTransparentBackground()
            standardAppearance.backgroundColor = UIColor.systemGray6
            UITabBar.appearance().standardAppearance = standardAppearance
            
        }
    }
    
    func handleRouteNavigation(
        route: Route,
        path: Binding<NavigationPath>
    ) -> some View {
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
        switch route {
        case .details(let content):
            return ShowDetailsScreen(detailsViewModel: ViewModelsModule().detailsViewModel, content: content, nav: detailsNav).eraseToAnyView()
        case .episodes:
            return DetailsEpisodesSheet(detailsViewModel: detailsViewModel).eraseToAnyView()
        case .cast:
            return DetailsCastCrewSheet(detailsViewModel: detailsViewModel, nav: detailsNav).eraseToAnyView()
        case .person(let personId):
            return PersonDetails(vm: personViewModel, personId: personId, nav: personNav).eraseToAnyView()
        case .media:
            return DetailsMediaSheet(detailsViewModel: detailsViewModel, nav: detailsNav).eraseToAnyView()
        case .filmCollection:
            return DetailsFilmCollectionSheet(detailsViewModel: detailsViewModel, nav: detailsNav).eraseToAnyView()
        case .personShows:
            return PersonShowsDetails(vm: personViewModel, nav: personNav).eraseToAnyView()
        case .personMovies:
            return PersonMoviesDetails(vm: personViewModel, nav: personNav).eraseToAnyView()
        case .personPhotos:
            return PersonPhotosDetails(vm: personViewModel).eraseToAnyView()
        case .newReleases:
            return NewReleasesScreen(discoverViewModel: discoverViewModel, nav: discoverNav(path: $path2))
        case .recommended:
            return RecommendedScreen(discoverViewModel: discoverViewModel, nav: recommendedNav)
        case .trending:
            return TrendingScreen(discoverViewModel: discoverViewModel, nav: discoverNav(path: $path2))
        }
    }
}

extension View {
    func eraseToAnyView() -> AnyView {
        AnyView(self)
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
    func styleToolbar(title: String) -> some View {
        return self.navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Text(title)
                        .font(.title2.bold())
                }
                
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
