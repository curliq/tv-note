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
    
    let val2:() -> Void
    
    init(v2: @escaping () -> Void) {
        val2 = v2
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.WatchingScreenViewController(onNav: val2)
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ShowDetailsScreen: UIViewControllerRepresentable {
    
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.ShowDetailsScreenViewController()
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
