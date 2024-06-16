package com.free.tvtracker.activities.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.free.tvtracker.activities.main.MainActivity
import com.free.tvtracker.activities.welcome.WelcomeActivity
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen visible for this Activity.
        splashScreen.setKeepOnScreenCondition { true }

        val splashViewModel: SplashViewModel by viewModel()
        when (splashViewModel.initialDestination()) {
            SplashViewModel.Destination.Welcome -> {
                startActivity(Intent(this, WelcomeActivity::class.java))
            }

            SplashViewModel.Destination.Home -> {
                startActivity(Intent(this, MainActivity::class.java))
            }

            SplashViewModel.Destination.Error -> {
                startActivity(Intent(this, SplashErrorActivity::class.java))
            }
        }
        finish()
    }
}
