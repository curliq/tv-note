package com.free.tvtracker.activities.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import com.free.tvtracker.activities.splash.SplashActivity
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.expect.data.DatabaseNameAndroid
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.settings.SettingsScreen
import com.free.tvtracker.ui.settings.SettingsScreenNavAction
import com.free.tvtracker.ui.settings.SettingsViewModel
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: SettingsViewModel = get()
            if (viewModel.logout.collectAsState().value) {
                this.deleteDatabase(DatabaseNameAndroid)
                val context = LocalContext.current
                val intent = Intent(context, SplashActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                (context as Activity).finish()
                Runtime.getRuntime().exit(0)
            }

            TvTrackerTheme {
                val navAction = { action: SettingsScreenNavAction ->
                    when (action) {
                        SettingsScreenNavAction.GoLogin -> {
                            startActivity(Intent(this, LoginActivity::class.java))
                        }

                        SettingsScreenNavAction.GoSignup -> {
                            startActivity(Intent(this, SignupActivity::class.java))
                        }

                        is SettingsScreenNavAction.GoBrowser -> {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(action.url))
                            startActivity(browserIntent)
                        }
                    }
                }
                val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        MediumTopAppBar(
                            title = {
                                Text(
                                    text = "Settings",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            },
                            scrollBehavior = scrollBehavior,
                            navigationIcon = {
                                IconButton(onClick = { this.finish() }) {
                                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "")
                                }
                            }
                        )
                    }
                ) { padding ->
                    SettingsScreen(viewModel = viewModel, navAction, padding)
                }
            }
        }
    }
}
