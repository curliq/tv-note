package com.free.tvtracker.core.ui

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.free.tvtracker.data.session.SessionRepository
import com.free.tvtracker.ui.settings.SettingsUiModel
import com.free.tvtracker.ui.settings.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

open class BaseActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by inject<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEdgeToEdge()

        // load the session for every activity, so that any activity can be restored in case of a crash
        val sessionRepository = get<SessionRepository>()
        sessionRepository.loadSessionIfMissing()

        this.lifecycleScope.launch {
            settingsViewModel.theme.collectLatest {
                val mode = when (it) {
                    SettingsUiModel.Theme.System, null -> AppCompatDelegate.MODE_NIGHT_NO
                    SettingsUiModel.Theme.Dark -> AppCompatDelegate.MODE_NIGHT_YES
                    SettingsUiModel.Theme.Light -> AppCompatDelegate.MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setEdgeToEdge()
    }

    private fun setEdgeToEdge() {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
    }
}
