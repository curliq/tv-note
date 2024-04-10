package com.free.tvtracker.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.screens.details.DetailsScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class ShowDetailsActivity : BaseActivity() {
    companion object Extras {
        val EXTRA_SHOW_ID = "EXTRA_SHOW_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getIntExtra(EXTRA_SHOW_ID, -1).let { showId ->
            if (showId != -1) {
                setContent {
                    DetailsScreen(viewModel = koinViewModel(parameters = { parametersOf(showId) }), showId = showId)
                }
            }
        }
    }
}
