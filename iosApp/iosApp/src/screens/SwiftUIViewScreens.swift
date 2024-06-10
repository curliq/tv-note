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
//    let model: WatchingItemUiModel

    init(navigate: @escaping (WatchingScreenNavAction) -> Void,  watchingViewModel: WatchingViewModel) {
        self.navigate = navigate
        self.watchingViewModel = watchingViewModel
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.WatchingScreenViewController(
            navigate: navigate,
            viewModel: watchingViewModel
        )
//        MainViewControllerKt.WatchingItemViewController(
//            navigate: navigate,
//            watchingViewModel: watchingViewModel,
//            uiModel: model
//        )
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ShowDetailsScreen: UIViewControllerRepresentable {

    let detailsViewModel: DetailsViewModel
    let showId: Int32

    init(detailsViewModel: DetailsViewModel, showId: Int32) {
        self.detailsViewModel = detailsViewModel
        self.showId = showId
    }

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.ShowDetailsScreenViewController(detailsViewModel: detailsViewModel, showId: showId, navigate: { _ in })
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct AddTrackedScreen: UIViewControllerRepresentable {

    let addTrackedViewModel: AddTrackedViewModel

    init(addTrackedViewModel: AddTrackedViewModel) {
        self.addTrackedViewModel = addTrackedViewModel
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.AddTrackedScreenViewController(
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
        MainViewControllerKt.FinishedScreenViewController(
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
        MainViewControllerKt.WatchlistScreenViewController(
            navigate: {_ in },
            viewModel: watchlistViewModel
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct DiscoverScreen: UIViewControllerRepresentable {

    let discoverViewModel: DiscoverViewModel

    init(discoverViewModel: DiscoverViewModel) {
        self.discoverViewModel = discoverViewModel
    }

    func makeUIViewController(context: Context) -> UIViewController {
        
        let c = MainViewControllerKt.DiscoverScreenViewController(
            navigate: { _ in },
            viewModel: discoverViewModel
        )
        
        return c
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct SettingsScreen: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.SettingsScreenViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

#Preview {
    SettingsScreen()
}
