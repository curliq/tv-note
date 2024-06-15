package com.free.tvtracker.ui.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.free.tvtracker.expect.OsPlatform
import com.free.tvtracker.ui.common.composables.LoadingIndicator
import com.free.tvtracker.ui.common.theme.TvTrackerTheme

@Composable
fun WelcomeScreen(
    navigateHome: () -> Unit,
    viewModel: WelcomeViewModel,
    modifier: Modifier = Modifier,
) {
    val status = viewModel.status.collectAsState().value
    if (status == WelcomeViewModel.Status.GoToHome) {
        navigateHome()
    }
    TvTrackerTheme {
        Scaffold(modifier = modifier) {
            Column {
                Text("Welcome TV Tracker")
                Text("Keep track of all the shows and movies you watch in one place")
                Text("Get notified when new episodes come out")
                Text("Look up shows, movies and people")
                Text("Why this app is nice")
                Text("- It's free")
                Text("- Doesn't have ads")
                Text("- No account required")
                Text("- Data is backed up (email optional)")
                if (OsPlatform().get() == OsPlatform.Platform.Android) {
                    Text("- Looks nice imo")
                }

                Button(modifier = Modifier.width(150.dp), onClick = viewModel::goToHome) {
                    Text("OK")
                    Spacer(Modifier.width(16.dp))
                    Box(contentAlignment = Alignment.Center, ) {
                        if (status == WelcomeViewModel.Status.Loading) {
                            LoadingIndicator(
                                modifier = Modifier.height(24.dp).aspectRatio(1f),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(Icons.AutoMirrored.Rounded.ArrowForward, "ok")
                        }
                    }
                }
            }
        }
    }
}
