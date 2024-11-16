package com.free.tvtracker.activities.splash

import android.os.Bundle
import androidx.activity.compose.setContent
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.splash.SplashErrorScreen

class SplashErrorActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvTrackerTheme {
                SplashErrorScreen()
            }
        }
    }
}
