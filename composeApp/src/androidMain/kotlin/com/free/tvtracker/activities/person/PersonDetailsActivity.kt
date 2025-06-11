package com.free.tvtracker.activities.person

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.free.tvtracker.activities.showdetails.ShowDetailsActivity
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.core.ui.BaseActivity
import com.free.tvtracker.ui.person.PersonScreen
import com.free.tvtracker.ui.person.PersonScreenNavAction
import com.free.tvtracker.ui.person.PersonViewModel
import com.free.tvtracker.ui.person.dialogs.PersonMoviesSheet
import com.free.tvtracker.ui.person.dialogs.PersonPhotosSheet
import com.free.tvtracker.ui.person.dialogs.PersonShowsSheet
import org.koin.androidx.compose.koinViewModel

class PersonDetailsActivity : BaseActivity() {
    companion object Extras {
        const val EXTRA_PERSON_ID = "EXTRA_SHOW_ID"
    }

    enum class PersonDetailsNavDestinations {
        SHOWS,
        MOVIES,
        PHOTOS
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val personId = intent.getIntExtra(EXTRA_PERSON_ID, -1)
        setContent {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            val sheetState = rememberModalBottomSheetState()
            var showBottomSheet: PersonDetailsNavDestinations? by remember { mutableStateOf(null) }
            val context = LocalActivity.current as PersonDetailsActivity
            val modalMaxHeight = LocalWindowInfo.current.containerSize.height.dp.times(0.7f)
            val navActions: (PersonScreenNavAction) -> Unit = { action ->
                when (action) {
                    is PersonScreenNavAction.GoShowDetails -> {
                        context.startActivity(
                            ShowDetailsActivity.create(context, action.tmdbShowId, action.isTvShow)
                        )
                    }

                    PersonScreenNavAction.GoAllPhotos -> {
                        showBottomSheet = PersonDetailsNavDestinations.PHOTOS
                    }

                    PersonScreenNavAction.GoAllShows -> {
                        showBottomSheet = PersonDetailsNavDestinations.SHOWS
                    }

                    PersonScreenNavAction.GoAllMovies -> {
                        showBottomSheet = PersonDetailsNavDestinations.MOVIES
                    }

                    is PersonScreenNavAction.GoInstagram -> {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW).setData(Uri.parse(action.url))
                        )
                    }
                }
            }
            TvTrackerTheme {
                val viewModel: PersonViewModel = koinViewModel(viewModelStoreOwner = context)
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            title = { },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.mediumTopAppBarColors(),
                            navigationIcon = {
                                IconButton(onClick = { this.finish() }) {
                                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "")
                                }
                            },
                        )
                    },
                ) { padding ->
                    PersonScreen(
                        viewModel = viewModel,
                        personId = personId,
                        navAction = navActions,
                        modifier = Modifier
                            .padding(padding)
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                    )
                    if (showBottomSheet != null) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                showBottomSheet = null
                            },
                            sheetState = sheetState,
                            contentWindowInsets = { WindowInsets(0, 0, 0, 0) } // draw behind navbar
                        ) {
                            Box(Modifier.heightIn(0.dp, modalMaxHeight)) {
                                when (showBottomSheet) {
                                    PersonDetailsNavDestinations.SHOWS -> {
                                        PersonShowsSheet(
                                            viewModel = koinViewModel(viewModelStoreOwner = context),
                                            navActions = navActions,
                                            padding.calculateBottomPadding().value
                                        )
                                    }

                                    PersonDetailsNavDestinations.MOVIES -> {
                                        PersonMoviesSheet(
                                            viewModel = koinViewModel(viewModelStoreOwner = context),
                                            navActions = navActions,
                                            padding.calculateBottomPadding().value
                                        )
                                    }

                                    PersonDetailsNavDestinations.PHOTOS -> {
                                        PersonPhotosSheet(
                                            viewModel = koinViewModel(viewModelStoreOwner = context),
                                            padding.calculateBottomPadding().value
                                        )
                                    }


                                    null -> {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
