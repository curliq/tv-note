package com.free.tvtracker.activities.main.bottomnav

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.free.tvtracker.activities.main.AppNavController
import com.free.tvtracker.activities.main.MainActivity
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@ExperimentalMaterial3Api
@ExperimentalMaterialNavigationApi
@Composable
fun MainNavHost(
    padding: PaddingValues,
    showBottomSheet: MutableState<WatchlistNavDestinations?>,
    navController: AppNavController
) {
    val context = LocalActivity.current as MainActivity
    NavHost(
        navController = navController.rememberNavController(),
        startDestination = AppNavDestinations.WATCHING.id,
        modifier = Modifier.padding(padding),
        enterTransition = { fadeIn(animationSpec = tween(durationMillis = 0)) },
        exitTransition = { fadeOut(animationSpec = tween(durationMillis = 0)) },
        popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 0)) },
        popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 0)) }
    ) {
        mainNavGraph(navController, showBottomSheet, context)
    }
}
