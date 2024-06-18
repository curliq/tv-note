package com.free.tvtracker.activities.settings

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
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.settings.LoginScreen
import com.free.tvtracker.ui.settings.LoginScreenNavAction
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvTrackerTheme {
                val navAction = { action: LoginScreenNavAction ->
                    when (action) {
                        LoginScreenNavAction.GoBack -> {
                            finish()
                        }
                    }
                }
                Scaffold(
                    topBar = {
                        MediumTopAppBar(
                            title = {
                                Text(
                                    text = "Login",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            },
                            scrollBehavior = TopAppBarDefaults
                                .exitUntilCollapsedScrollBehavior(rememberTopAppBarState()),
                            navigationIcon = {
                                IconButton(onClick = { this.finish() }) {
                                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "")
                                }
                            }
                        )
                    }
                ) { padding ->
                    LoginScreen(viewModel = get(), navAction, padding)
                }
            }
        }
    }
}
