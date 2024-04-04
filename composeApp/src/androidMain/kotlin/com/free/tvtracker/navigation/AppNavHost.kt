package com.free.tvtracker.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.free.tvtracker.MainActivity
import com.free.tvtracker.navigation.AppNavController.NavDestinations
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@ExperimentalMaterial3Api
@ExperimentalMaterialNavigationApi
@Composable
fun MainNavHost(padding: PaddingValues, navController: AppNavController) {
    val context = LocalContext.current as MainActivity
    NavHost(
        navController = navController.rememberNavController(),
        startDestination = NavDestinations.WATCHING.id,
        modifier = androidx.compose.ui.Modifier.padding(padding)
    ) {
        mainNavGraph(navController, context)
    }
}
