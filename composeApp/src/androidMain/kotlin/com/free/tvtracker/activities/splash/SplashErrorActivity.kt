package com.free.tvtracker.activities.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.ui.common.theme.TvTrackerTheme

@SuppressLint("CustomSplashScreen")
class SplashErrorActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
    }
}
