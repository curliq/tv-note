package com.free.tvtracker.activities.settings

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import com.free.tvtracker.core.ui.BaseActivity
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
            val context = LocalContext.current
            val viewModel: SettingsViewModel = get()
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

                        is SettingsScreenNavAction.EmailSupport -> {
                            val emailIntent = Intent(Intent.ACTION_SEND)
                            emailIntent.setType("plain/text")
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(action.email))
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support from Android app")
                            context.startActivity(Intent.createChooser(emailIntent, "Send mail to ${action.email}"))
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
                                    Icon(
                                        Icons.AutoMirrored.Rounded.ArrowBack,
                                        "",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
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
