package com.free.tvtracker.activities.welcome

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.free.tvtracker.activities.main.MainActivity
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.welcome.WelcomeScreen
import org.koin.androidx.compose.koinViewModel

class WelcomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvTrackerTheme {
                Scaffold { padding ->
                    WelcomeScreen(
                        navigateHome = {
                            startActivity(Intent(this, MainActivity::class.java))
                        },
                        viewModel = koinViewModel(),
                        openUrl = {},
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
