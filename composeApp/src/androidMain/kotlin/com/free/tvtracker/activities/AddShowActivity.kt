package com.free.tvtracker.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.free.tvtracker.R
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.screens.search.AddTrackedScreen
import com.free.tvtracker.screens.search.AddTrackedViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
class AddShowActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: AddTrackedViewModel = koinViewModel()
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
            TvTrackerTheme {
                Scaffold(
                    topBar = {
                        MediumTopAppBar(
                            title = { Text(text = "Add to tracking") },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.mediumTopAppBarColors(),
                            navigationIcon = {
                                IconButton(onClick = { this.finish() }) {
                                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "")
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                viewModel.focusSearch()
                            },
                            modifier = Modifier.imePadding()
                        ) {
                            Icon(painterResource(id = R.drawable.ic_keyboard_up_ic), "")
                        }
                    }
                ) { padding ->
                    AddTrackedScreen(viewModel = viewModel, modifier = Modifier.padding(padding))
                }
            }
        }
    }
}
