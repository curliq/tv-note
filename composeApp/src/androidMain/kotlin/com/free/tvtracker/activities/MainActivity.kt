package com.free.tvtracker.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.navigation.bottom.AppNavController
import com.free.tvtracker.navigation.bottom.BottomNavBar
import com.free.tvtracker.navigation.bottom.MainNavHost
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterial3Api::class)
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
            val appNavController = remember { AppNavController() }
            val bottomSheetNavigator = appNavController.rememberSheetNavigator()
            TvTrackerTheme {
                ModalBottomSheetLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    bottomSheetNavigator = bottomSheetNavigator
                ) {
                    Scaffold(
                        bottomBar = {
                            BottomNavBar(
                                appNavController = appNavController,
                            )
                        },
                        topBar = {
                            MediumTopAppBar(
                                title = { Text(text = "Watching") },
                                scrollBehavior = scrollBehavior,
                            )
                        }
                    ) { padding ->
                        MainNavHost(padding = padding, navController = appNavController)
                    }
                }
            }
        }
    }
}
