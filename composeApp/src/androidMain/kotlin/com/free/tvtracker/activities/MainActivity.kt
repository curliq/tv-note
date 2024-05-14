package com.free.tvtracker.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hierarchy
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.navigation.AppNavController
import com.free.tvtracker.navigation.bottomnav.BottomNavBar
import com.free.tvtracker.navigation.bottomnav.BottomNavBarItems
import com.free.tvtracker.navigation.bottomnav.MainNavHost
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterial3Api::class)
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appNavController = remember { AppNavController() }
            val currentDestination = appNavController.rememberCurrentDestination()
            val scrollBehaviors = BottomNavBarItems.entries.map {
                it.destinationId to TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
            }
            val scroll = scrollBehaviors.firstOrNull { scroll ->
                currentDestination?.hierarchy?.any { dest -> dest.route == scroll.first } == true
            }?.second
            TvTrackerTheme {
                Scaffold(
                    bottomBar = { BottomNavBar(appNavController = appNavController) },
                    topBar = {
                        MediumTopAppBar(
                            title = {
                                BottomNavBarItems.entries.forEach { item ->
                                    if (currentDestination?.hierarchy?.any { it.route == item.destinationId } == true) {
                                        Text(
                                            text = item.title,
                                            style = TvTrackerTheme.Typography.headlineMedium
                                        )
                                    }
                                }
                            },
                            scrollBehavior = scroll,
                        )
                    }
                ) { padding ->
                    MainNavHost(padding = padding, navController = appNavController)
                }
            }
        }
    }
}
