package com.free.tvtracker.activities.main.bottomnav

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hierarchy
import com.free.tvtracker.activities.main.AppNavController

@Composable
fun BottomNavBar(appNavController: AppNavController) {
    NavigationBar {
        val currentDestination = appNavController.rememberCurrentDestination()
        BottomNavBarItems.entries.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.destinationId } == true
            BottomNavBarItem(
                selected = selected,
                label = screen.label,
                icon = screen.icon,
                iconSelected = screen.iconSelected,
                onNavClick = {
                    appNavController.navigate(destinationId = screen.destinationId)
                },
            )
        }
    }
}
