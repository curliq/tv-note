package com.free.tvtracker.navigation.bottom

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.free.tvtracker.activities.MainActivity
import com.free.tvtracker.navigation.bottom.AppNavController.NavDestinations
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@ExperimentalMaterial3Api
@ExperimentalMaterialNavigationApi
@Composable
fun MainNavHost(padding: PaddingValues, navController: AppNavController) {
    val context = LocalContext.current as MainActivity
    NavHost(
        navController = navController.rememberNavController(),
        startDestination = NavDestinations.WATCHING.id,
        modifier = Modifier.padding(padding),
        enterTransition = { fadeIn(animationSpec = tween(durationMillis = 220)) },
        exitTransition = { fadeOut(animationSpec = tween(durationMillis = 220)) },
        popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 100)) },
        popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 100)) }
    ) {
        mainNavGraph(navController, context)
    }
}
