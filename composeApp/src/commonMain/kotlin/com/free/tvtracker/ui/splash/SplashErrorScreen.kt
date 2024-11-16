package com.free.tvtracker.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.theme.TvTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashErrorScreen() {
    TvTrackerTheme {
        Scaffold(
            topBar = {
                MediumTopAppBar(
                    title = { Text(text = "Error") },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(),
                )
            }
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                Column(Modifier.padding(horizontal = TvTrackerTheme.sidePadding, vertical = 24.dp)) {

                    Text(
                        "Something went wrong fetching your data. If your data is backed up with an account " +
                            "then please reinstall the app, otherwise please contact the developer (me) to help" +
                            " restoring your data."
                    )
                }
            }
        }
    }
}
