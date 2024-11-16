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
    
    let detailsViewModel: DetailsViewModel
    let content: DetailsViewModel.LoadContent
    let nav: (DetailsScreenNavAction) -> Void
    
    init(detailsViewModel: DetailsViewModel, content: DetailsViewModel.LoadContent, nav: @escaping (DetailsScreenNavAction) -> Void) {
        self.detailsViewModel = detailsViewModel
        self.content = content
        self.nav = nav
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.ShowDetailsScreenViewController(
            detailsViewModel: detailsViewModel,
            content: content,
            navigate: nav
        )
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct DetailsEpisodesSheet: UIViewControllerRepresentable {
    
    let detailsViewModel: DetailsViewModel
    
    init(detailsViewModel: DetailsViewModel) {
        self.detailsViewModel = detailsViewModel
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.DetailsEpisodesViewController(
            viewModel: detailsViewModel
        )
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct DetailsMediaSheet: UIViewControllerRepresentable {
    
    let detailsViewModel: DetailsViewModel
    let nav: (DetailsScreenNavAction) -> Void
    
    init(detailsViewModel: DetailsViewModel, nav: @escaping (DetailsScreenNavAction) -> Void) {
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
    
    let detailsViewModel: DetailsViewModel
    let nav: (DetailsScreenNavAction) -> Void
    
    init(detailsViewModel: DetailsViewModel, nav: @escaping (DetailsScreenNavAction) -> Void) {
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
    
    let detailsViewModel: DetailsViewModel
    let nav: (DetailsScreenNavAction) -> Void
    
    init(detailsViewModel: DetailsViewModel, nav: @escaping (DetailsScreenNavAction) -> Void) {
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
    
    init(addTrackedViewModel: AddTrackedViewModel) {
        self.addTrackedViewModel = addTrackedViewModel
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.AddTrackedScreenViewController(
            addTrackedViewModel: addTrackedViewModel,
            navigate: { _ in  },
            originScreen: AddTrackedScreenOriginScreen.discover //todo
        )
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}


struct FinishedScreen: UIViewControllerRepresentable {
    
    let finishedViewModel: FinishedShowsViewModel
    
    init(finishedViewModel: FinishedShowsViewModel) {
        self.finishedViewModel = finishedViewModel
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.FinishedScreenViewController(
            navigate:{ _ in },
            viewModel: finishedViewModel
        )
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct WatchlistScreen: UIViewControllerRepresentable {
    
    let watchlistViewModel: WatchlistedShowsViewModel
    
    init(watchlistViewModel: WatchlistedShowsViewModel) {
        self.watchlistViewModel = watchlistViewModel
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.WatchlistScreenViewController(
            navigate: {_ in },
            viewModel: watchlistViewModel
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
    
    init(vm: WelcomeViewModel, nav: @escaping () -> Void) {
        self.vm = vm
        self.navHome = nav
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        ViewControllersKt.WelcomeScreenViewController(
            navigateHome: navHome,
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
