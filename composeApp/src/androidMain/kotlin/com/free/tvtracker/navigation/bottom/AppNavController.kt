package com.free.tvtracker.navigation.bottom

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialNavigationApi::class)
class AppNavController {

    enum class NavDestinations(val id: String) {
        WATCHING("watching"),
        FINISHED("finished"),
        WATCHLIST("watchlist"),
        DISCOVER("discover"),
        SETTINGS("settings"),
    }

    private var navHostController: NavHostController? = null
    private var bottomSheetNavigator: BottomSheetNavigator? = null

    @Composable
    fun rememberSheetNavigator(): BottomSheetNavigator {
        if (bottomSheetNavigator != null) {
            return bottomSheetNavigator!!
        }
        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )
        bottomSheetNavigator = remember { BottomSheetNavigator(sheetState) }
        return bottomSheetNavigator!!
    }

    @Composable
    fun rememberNavController(): NavHostController {
        if (navHostController != null) {
            return navHostController!!
        }
        val bottomSheetNavigator = rememberSheetNavigator()
        navHostController = rememberNavController(bottomSheetNavigator)
        return navHostController!!
    }

    fun pop() {
        navHostController?.popBackStack()
    }

    fun navigate(destinationId: String) {
        val navController = navHostController ?: throw IllegalStateException("Initialise the AppNavController first")
        navController.navigate(destinationId) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}
