//
//  SwiftUIViewScreens.swift
//  iosApp
//
//  Created by juja on 24/03/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

struct SwiftUIViewScreens: View {
    var body: some View {
        Text(/*@START_MENU_TOKEN@*/"Hello, World!"/*@END_MENU_TOKEN@*/)
    }
}

struct WatchingScreen: UIViewControllerRepresentable {
    
    let navigate: (NavAction) -> Void
    let watchingViewModel: WatchingViewModel

    init(navigate: @escaping (NavAction) -> Void,  watchingViewModel: WatchingViewModel) {
        self.navigate = navigate
        self.watchingViewModel = watchingViewModel
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.WatchingScreenViewController(
            navigate: navigate,
            watchingViewModel: watchingViewModel
        )
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
        MainViewControllerKt.ShowDetailsScreenViewController(detailsViewModel: detailsViewModel, showId: showId)
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct AddTrackedScreen: UIViewControllerRepresentable {

    let addTrackedViewModel: AddTrackedViewModel

    init(addTrackedViewModel: AddTrackedViewModel) {
        self.addTrackedViewModel = addTrackedViewModel
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.AddTrackedScreenViewController(addTrackedViewModel: addTrackedViewModel)
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}


struct FinishedScreen: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.FinishedScreenViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct WatchlistScreen: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.WatchlistScreenViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct DiscoverScreen: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.DiscoverScreenViewController()
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
