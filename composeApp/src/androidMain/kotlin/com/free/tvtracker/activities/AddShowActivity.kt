package com.free.tvtracker.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.free.tvtracker.core.theme.TvTrackerTheme
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.navigation.bottom.AppNavController
import com.free.tvtracker.screens.search.AddTrackedScreen
import com.free.tvtracker.screens.search.AddTrackedViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterial3Api::class)
class AddShowActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: AddTrackedViewModel = koinViewModel()


            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
            val appNavController = remember {
                AppNavController() //todo share instance
            }
            val bottomSheetNavigator = appNavController.rememberSheetNavigator()
            TvTrackerTheme {
                ModalBottomSheetLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    bottomSheetNavigator = bottomSheetNavigator
                ) {
                    Scaffold(
                        topBar = {
                            MediumTopAppBar(
                                title = { Text(text = "Add to tracking") },
                                scrollBehavior = scrollBehavior,
                                colors = TopAppBarDefaults.mediumTopAppBarColors()
                            )
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = {
                                    viewModel.focusSearch()
                                },
                                modifier = Modifier.imePadding()
                            ) {
                                Icon(Icons.Default.KeyboardArrowUp, "")
                            }
                        }
                    ) { padding ->
                        AddTrackedScreen(viewModel = viewModel, modifier = Modifier.padding(padding))
                    }
                }
            }
        }
    }
}
