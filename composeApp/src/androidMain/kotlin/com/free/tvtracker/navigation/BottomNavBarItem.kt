package com.free.tvtracker.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.deloitteshop.theme.ThemeColors
import com.free.tvtracker.R
import com.free.tvtracker.Typography

enum class BottomNavBarItems(val label: String, val icon: Int, val iconSelected: Int, val destinationId: String) {
    WATCHING(
        "Watching",
        R.drawable.ic_live_tv_24px,
        R.drawable.ic_live_tv_filled_24px,
        AppNavController.NavDestinations.WATCHING.id
    ),
    FINISHED(
        "Finished",
        R.drawable.ic_flag_24px,
        R.drawable.ic_flag_filled_24px,
        AppNavController.NavDestinations.FINISHED.id
    ),
    WATCHLIST(
        "Watchlist",
        R.drawable.ic_star_24px,
        R.drawable.ic_star_filled_24px,
        AppNavController.NavDestinations.WATCHLIST.id
    ),
    DISCOVER(
        "Discover",
        R.drawable.ic_explore_24px,
        R.drawable.ic_explore_filled_24px,
        AppNavController.NavDestinations.DISCOVER.id
    ),
    SETTINGS(
        "Settings",
        R.drawable.ic_settings_24px,
        R.drawable.ic_settings_filled_24px,
        AppNavController.NavDestinations.SETTINGS.id
    ),
}

@Composable
fun RowScope.BottomNavBarItem(selected: Boolean, label: String, icon: Int, iconSelected: Int, onNavClick: () -> Unit) {
    BottomNavigationItem(
        icon = {
            Icon(
                painterResource(id = if (selected) iconSelected else icon),
                contentDescription = null,
                tint = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    ThemeColors.PrimaryDisabled
                }
            )
        },
        label = {
            Text(
                label,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    ThemeColors.PrimaryDisabled
                },
                style = Typography.labelMedium
            )
        },
        selected = selected,
        onClick = onNavClick
    )
}
