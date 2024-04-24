package com.free.tvtracker.navigation.bottom

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.free.tvtracker.R

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
    NavigationBarItem(
        icon = {
            Icon(
                painterResource(id = if (selected) iconSelected else icon),
                contentDescription = null,
            )
        },
        label = {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                softWrap = false,
            )
        },
        selected = selected,
        onClick = onNavClick,
    )
}
