//
//  SplashScreen.swift
//  iosApp
//
//  Created by juja on 09/11/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import UIKit
import SwiftUI
import ComposeApp

struct SplashScreen: View {
    
    @Environment(\.colorScheme) var colorScheme
    @Environment(\.openURL) var openURL
    
    let vm = ViewModelsModule().splashViewModel
    let welcomeVm = ViewModelsModule().welcomeViewModel
    
    @State private var showHome = false
    
    var body: some View {
        VStack {
            let dest = vm.initialDestination()
            switch dest {
            case SplashViewModel.Destination.welcome:
                if (!showHome) {
                    let nav: () -> Void = {
                        showHome = true
                    }
                    WelcomeScreen(vm: welcomeVm, nav: nav, openUrl: { url in
                        if #available(iOS 10.0, *) {
                            UIApplication.shared.open(URL(string:url)!)
                        } else {
                            UIApplication.shared.openURL(URL(string:url)!)
                        }
                    })
                } else {
                    ContentView()
                }
            case SplashViewModel.Destination.home:
                ContentView()
            case SplashViewModel.Destination.error:
                SplashErrorScreen()
            default:
                SplashErrorScreen()
            }
        }.nestedBackgroundColor(isDarkTheme: colorScheme == .dark)
    }
}
