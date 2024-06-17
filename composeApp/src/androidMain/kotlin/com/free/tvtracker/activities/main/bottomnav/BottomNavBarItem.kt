package com.free.tvtracker.activities.main.bottomnav

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.free.tvtracker.R

enum class BottomNavBarItems(val label: String, val title: String, val icon: Int, val iconSelected: Int, val destinationId: String) {
    WATCHING(
        "Watching",
        "Currently Watching",
        R.drawable.ic_live_tv_24px,
        R.drawable.ic_live_tv_filled_24px,
        AppNavDestinations.WATCHING.id
    ),
    FINISHED(
        "Finished",
        "Finished Watching",
        R.drawable.ic_flag_24px,
        R.drawable.ic_flag_filled_24px,
        AppNavDestinations.FINISHED.id
    ),
    WATCHLIST(
        "Watchlist",
        "Watchlist",
        R.drawable.ic_bookmark,
        R.drawable.ic_bookmark_filled,
        AppNavDestinations.WATCHLIST.id
    ),
    DISCOVER(
        "Discover",
        "Discover",
        R.drawable.ic_explore_24px,
        R.drawable.ic_explore_filled_24px,
        AppNavDestinations.DISCOVER.id
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
