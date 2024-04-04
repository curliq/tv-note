package com.free.tvtracker.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.deloitteshop.theme.ThemeColors

@Composable
fun BottomNavBar(appNavController: AppNavController, ) {
    val navController = appNavController.rememberNavController()
    BottomNavigation(
        backgroundColor = ThemeColors.SurfaceSecondary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        BottomNavBarItems.entries.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.destinationId } == true
            BottomNavBarItem(
                selected = selected,
                label = screen.label,
                icon = screen.icon,
                iconSelected = screen.iconSelected,
                onNavClick = {
                    appNavController.navigate(destinationId = screen.destinationId)
                })
        }
    }
}
