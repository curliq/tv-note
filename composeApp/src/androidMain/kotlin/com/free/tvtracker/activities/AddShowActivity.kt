package com.free.tvtracker.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.screens.search.AddTrackedScreen
import org.koin.androidx.compose.koinViewModel

class AddShowActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AddTrackedScreen(viewModel = koinViewModel())
        }
    }
}
