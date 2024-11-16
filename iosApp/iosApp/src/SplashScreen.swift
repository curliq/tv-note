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
    
    let vm = ViewModelsModule().splashViewModel
    let welcomeVm = ViewModelsModule().welcomeViewModel

    @State private var showHome = false
    
    var body: some View {
        let dest = vm.initialDestination()
        switch dest {
        case SplashViewModel.Destination.welcome:
            if (!showHome) {
                let nav: () -> Void = {
                    showHome = true
                }
                WelcomeScreen(vm: welcomeVm, nav: nav)
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
    }
}
