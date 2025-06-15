//
//  SwiftUIViewScreens.swift
//  iosApp
//
//  Created by juja on 24/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import UIKit
import ComposeApp

struct WatchingScreen: UIViewControllerRepresentable {

    let navigate: (WatchingScreenNavAction) -> Void
    let watchingViewModel: WatchingViewModel

    init(navigate: @escaping (WatchingScreenNavAction) -> Void,  watchingViewModel: WatchingViewModel) {
        self.navigate = navigate
        self.watchingViewModel = watchingViewModel
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.WatchingScreenViewController(
            navigate: navigate,
            viewModel: watchingViewModel
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ShowDetailsScreen: UIViewControllerRepresentable {

    let detailsViewModel: ContentDetailsViewModel
    let content: ContentDetailsViewModel.LoadContent
    let nav: (DetailsScreenNavAction) -> Void

    init(detailsViewModel: ContentDetailsViewModel, content: ContentDetailsViewModel.LoadContent, nav: @escaping (DetailsScreenNavAction) -> Void) {
        self.detailsViewModel = detailsViewModel
        self.content = content
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.ShowDetailsScreenViewController(
            contentDetailsViewModel: detailsViewModel,
            content: content,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct DetailsEpisodesSheet: UIViewControllerRepresentable {

    let detailsViewModel: ContentDetailsViewModel

    init(detailsViewModel: ContentDetailsViewModel) {
        self.detailsViewModel = detailsViewModel
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.DetailsEpisodesViewController(
            viewModel: detailsViewModel
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct DetailsReviewsSheet: UIViewControllerRepresentable {

    let detailsViewModel: ContentDetailsViewModel

    init(detailsViewModel: ContentDetailsViewModel) {
        self.detailsViewModel = detailsViewModel
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.DetailsReviewsViewController(
            viewModel: detailsViewModel
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct DetailsMediaSheet: UIViewControllerRepresentable {

    let detailsViewModel: ContentDetailsViewModel
    let nav: (DetailsScreenNavAction) -> Void

    init(detailsViewModel: ContentDetailsViewModel, nav: @escaping (DetailsScreenNavAction) -> Void) {
        self.detailsViewModel = detailsViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.DetailsMediaViewController(
            viewModel: detailsViewModel,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct DetailsCastCrewSheet: UIViewControllerRepresentable {

    let detailsViewModel: ContentDetailsViewModel
    let nav: (DetailsScreenNavAction) -> Void

    init(detailsViewModel: ContentDetailsViewModel, nav: @escaping (DetailsScreenNavAction) -> Void) {
        self.detailsViewModel = detailsViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.DetailsCastCrewViewController(
            viewModel: detailsViewModel,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct PersonDetails: UIViewControllerRepresentable {

    let vm: PersonViewModel
    let nav: (PersonScreenNavAction) -> Void
    let personId: Int32

    init(vm: PersonViewModel, personId: Int32, nav: @escaping (PersonScreenNavAction) -> Void) {
        self.vm = vm
        self.nav = nav
        self.personId = personId
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.PersonDetailsViewController(
            viewModel: vm,
            personId: personId,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct PersonShowsDetails: UIViewControllerRepresentable {

    let vm: PersonViewModel
    let nav: (PersonScreenNavAction) -> Void

    init(vm: PersonViewModel, nav: @escaping (PersonScreenNavAction) -> Void) {
        self.vm = vm
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.PersonShowsViewController(
            viewModel: vm,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct PersonMoviesDetails: UIViewControllerRepresentable {

    let vm: PersonViewModel
    let nav: (PersonScreenNavAction) -> Void

    init(vm: PersonViewModel, nav: @escaping (PersonScreenNavAction) -> Void) {
        self.vm = vm
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.PersonMoviesViewController(
            viewModel: vm,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct PersonPhotosDetails: UIViewControllerRepresentable {

    let vm: PersonViewModel

    init(vm: PersonViewModel) {
        self.vm = vm
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.PersonPhotosViewController(
            viewModel: vm
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct DetailsFilmCollectionSheet: UIViewControllerRepresentable {

    let detailsViewModel: ContentDetailsViewModel
    let nav: (DetailsScreenNavAction) -> Void

    init(detailsViewModel: ContentDetailsViewModel, nav: @escaping (DetailsScreenNavAction) -> Void) {
        self.detailsViewModel = detailsViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.DetailsFilmCollectionViewController(
            viewModel: detailsViewModel,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct AddTrackedScreen: UIViewControllerRepresentable {

    let addTrackedViewModel: AddTrackedViewModel
    let origin: AddTrackedScreenOriginScreen
    let nav: (AddTrackedScreenNavAction) -> Void

    init(addTrackedViewModel: AddTrackedViewModel, origin: AddTrackedScreenOriginScreen, nav: @escaping (AddTrackedScreenNavAction) -> Void) {
        self.addTrackedViewModel = addTrackedViewModel
        self.origin = origin
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.AddTrackedScreenViewController(
            addTrackedViewModel: addTrackedViewModel,
            navigate: nav,
            originScreen: origin
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct WatchlistsScreen: UIViewControllerRepresentable {

    let watchlistsViewModel: WatchlistsViewModel
    let nav: (WatchlistsScreenNavAction) -> Void

    init(watchlistsViewModel: WatchlistsViewModel, nav: @escaping (WatchlistsScreenNavAction) -> Void) {
        self.watchlistsViewModel = watchlistsViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.WatchlistsScreenViewController(
            navigate: nav,
            viewModel: watchlistsViewModel
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct WatchlistDetailsScreen: UIViewControllerRepresentable {

    let watchlistDetailsViewModel: WatchlistDetailsViewModel
    let content: WatchlistDetailsViewModel.LoadContent
    let nav: (WatchlistDetailsScreenNavAction) -> Void
    
    init(watchlistDetailsViewModel: WatchlistDetailsViewModel, content: WatchlistDetailsViewModel.LoadContent, nav: @escaping (WatchlistDetailsScreenNavAction) -> Void) {
        self.watchlistDetailsViewModel = watchlistDetailsViewModel
        self.content = content
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.WatchlistDetailsScreenViewController(
            watchlistDetailsViewModel: watchlistDetailsViewModel,
            content: content,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct CreateListSheet: UIViewControllerRepresentable {
    let watchlistsViewModel: WatchlistsViewModel
    let nav: (WatchlistsScreenNavAction) -> Void

    init(watchlistsViewModel: WatchlistsViewModel, nav: @escaping (WatchlistsScreenNavAction) -> Void) {
        self.watchlistsViewModel = watchlistsViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        return ViewControllersKt.WatchlistAddSheetViewController(
            viewModel: watchlistsViewModel,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct RenameListSheet: UIViewControllerRepresentable {
    let watchlistDetailsViewModel: WatchlistDetailsViewModel
    let nav: (WatchlistDetailsScreenNavAction) -> Void

    init(watchlistDetailsViewModel: WatchlistDetailsViewModel, nav: @escaping (WatchlistDetailsScreenNavAction) -> Void) {
        self.watchlistDetailsViewModel = watchlistDetailsViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        return ViewControllersKt.WatchlistDetailsRenameSheetViewController(
            viewModel: watchlistDetailsViewModel,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct WatchlistDetailsMenuSheet: UIViewControllerRepresentable {
    let watchlistDetailsViewModel: WatchlistDetailsViewModel
    let nav: (WatchlistDetailsScreenNavAction) -> Void

    init(watchlistDetailsViewModel: WatchlistDetailsViewModel, nav: @escaping (WatchlistDetailsScreenNavAction) -> Void) {
        self.watchlistDetailsViewModel = watchlistDetailsViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        return ViewControllersKt.WatchlistDetailsMenuSheetViewController(
            viewModel: watchlistDetailsViewModel,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct AddToListSheet: UIViewControllerRepresentable {
    let detailsViewModel: ContentDetailsViewModel
    let nav: (DetailsScreenNavAction) -> Void

    init(detailsViewModel: ContentDetailsViewModel, nav: @escaping (DetailsScreenNavAction) -> Void) {
        self.detailsViewModel = detailsViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        return ViewControllersKt.DetailsManageWatchlistsViewController(
            viewModel: detailsViewModel,
            navigate: nav
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct DiscoverScreen: UIViewControllerRepresentable {

    let discoverViewModel: DiscoverViewModel
    let nav: (DiscoverScreenNavActions) -> Void

    init(discoverViewModel: DiscoverViewModel, nav: @escaping (DiscoverScreenNavActions) -> Void) {
        self.discoverViewModel = discoverViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        return ViewControllersKt.DiscoverScreenViewController(
            navigate: nav,
            viewModel: discoverViewModel
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct RecommendedScreen: UIViewControllerRepresentable {

    let discoverViewModel: DiscoverViewModel
    let nav: (RecommendedScreenNavActions) -> Void

    init(discoverViewModel: DiscoverViewModel, nav: @escaping (RecommendedScreenNavActions) -> Void) {
        self.discoverViewModel = discoverViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        return ViewControllersKt.RecommendedScreenViewController(
            navigate: nav,
            viewModel: discoverViewModel
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct TrendingScreen: UIViewControllerRepresentable {

    let discoverViewModel: DiscoverViewModel
    let nav: (DiscoverScreenNavActions) -> Void

    init(discoverViewModel: DiscoverViewModel, nav: @escaping (DiscoverScreenNavActions) -> Void) {
        self.discoverViewModel = discoverViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        return ViewControllersKt.TrendingScreenViewController(
            navigate: nav,
            viewModel: discoverViewModel
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct NewReleasesScreen: UIViewControllerRepresentable {

    let discoverViewModel: DiscoverViewModel
    let nav: (DiscoverScreenNavActions) -> Void

    init(discoverViewModel: DiscoverViewModel, nav: @escaping (DiscoverScreenNavActions) -> Void) {
        self.discoverViewModel = discoverViewModel
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        return ViewControllersKt.NewReleasesScreenViewController(
            navigate: nav,
            viewModel: discoverViewModel
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct SettingsScreen: UIViewControllerRepresentable {

    let vm: SettingsViewModel
    let nav: (SettingsScreenNavAction) -> Void

    init(vm: SettingsViewModel, nav: @escaping (SettingsScreenNavAction) -> Void) {
        self.vm = vm
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.SettingsScreenViewController(
            navigate: nav,
            viewModel: vm
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct WelcomeScreen: UIViewControllerRepresentable {

    let vm: WelcomeViewModel
    let navHome: () -> Void
    let openUrl: (String) -> Void

    init(vm: WelcomeViewModel, nav: @escaping () -> Void, openUrl: @escaping (String) -> Void) {
        self.vm = vm
        self.navHome = nav
        self.openUrl = openUrl
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.WelcomeScreenViewController(
            navigateHome: navHome,
            openUrl: openUrl,
            viewModel: vm
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct LoginScreen: UIViewControllerRepresentable {

    let vm: LoginViewModel
    let nav: (LoginScreenNavAction) -> Void

    init(vm: LoginViewModel, nav: @escaping (LoginScreenNavAction) -> Void) {
        self.vm = vm
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.LoginScreenViewController(
            nav: nav,
            viewModel: vm
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct SignupScreen: UIViewControllerRepresentable {

    let vm: SignupViewModel
    let nav: (SignupScreenAction) -> Void

    init(vm: SignupViewModel, nav: @escaping (SignupScreenAction) -> Void) {
        self.vm = vm
        self.nav = nav
    }

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.SignupScreenViewController(
            nav: nav,
            viewModel: vm
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct SplashErrorScreen: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.SplashErrorScreenViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
