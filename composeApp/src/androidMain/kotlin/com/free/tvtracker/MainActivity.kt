package com.free.tvtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import besttvtracker.screens.watching.WatchingScreen
import besttvtracker.shared.WatchingViewModel
import com.free.tvtracker.navigation.AppNavController
import com.free.tvtracker.navigation.BottomNavBar
import com.free.tvtracker.navigation.MainNavHost
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appNavController = AppNavController()
            val bottomSheetNavigator = appNavController.rememberSheetNavigator()
            MaterialTheme {
                ModalBottomSheetLayout(modifier = Modifier.fillMaxSize(), bottomSheetNavigator = bottomSheetNavigator) {
                    Scaffold(bottomBar = {
                        BottomNavBar(
                            appNavController = appNavController,
                        )
                    }) { padding ->
                        MainNavHost(padding = padding, navController = appNavController)
                    }
                }
            }

        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MaterialTheme {
        WatchingScreen ({

        }, WatchingViewModel())
    }
}
