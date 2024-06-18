package com.free.tvtracker.activities.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import com.free.tvtracker.R
import com.free.tvtracker.activities.main.bottomnav.AppNavDestinations
import com.free.tvtracker.activities.main.bottomnav.BottomNavBar
import com.free.tvtracker.activities.main.bottomnav.BottomNavBarItems
import com.free.tvtracker.activities.main.bottomnav.MainNavHost
import com.free.tvtracker.activities.settings.SettingsActivity
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterial3Api::class)
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appNavController = remember { AppNavController() }
            val currentDestination = appNavController.rememberCurrentDestination()
            val scrollBehaviors = BottomNavBarItems.entries.map {
                it.destinationId to TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
            }
            val scroll = scrollBehaviors.firstOrNull { scroll ->
                currentDestination?.hierarchy?.any { dest -> dest.route == scroll.first } == true
            }?.second ?: TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
            TvTrackerTheme {
                Scaffold(
                    modifier = Modifier.nestedScroll(scroll.nestedScrollConnection),
                    bottomBar = { BottomNavBar(appNavController = appNavController) },
                    topBar = {
                        MediumTopAppBar(
                            title = {
                                BottomNavBarItems.entries.forEach { item ->
                                    if (currentDestination?.hierarchy?.any { it.route == item.destinationId } == true) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.headlineMedium
                                        )
                                    }
                                }
                            },
                            scrollBehavior = scroll,
                            actions = {
                                if (currentDestination?.route == AppNavDestinations.WATCHING.id) {
                                    IconButton(
                                        onClick = {
                                            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_settings_heart),
                                            contentDescription = "My Button"
                                        )
                                    }
                                }
                            }
                        )

                    }
                ) { padding ->
                    MainNavHost(padding = padding, navController = appNavController)
                }
            }
        }
        askNotificationPermission()
        val repo: SessionRepository = get()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            lifecycleScope.launch(Dispatchers.IO) {
                repo.postFcmToken(token)
            }
        })
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
