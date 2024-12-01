package com.free.tvtracker.ui.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import besttvtracker.composeapp.generated.resources.Res
import besttvtracker.composeapp.generated.resources.ic_movie
import com.free.tvtracker.ui.common.composables.ErrorScreen
import com.free.tvtracker.ui.common.composables.LoadingIndicator
import com.free.tvtracker.ui.common.composables.LoadingScreen
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.common.theme.TvTrackerTheme.sidePadding
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    navigateHome: () -> Unit,
    viewModel: WelcomeViewModel,
    modifier: Modifier = Modifier,
) {
    val status = viewModel.status.collectAsState().value
    if (status == WelcomeViewModel.Status.GoToHome) {
        navigateHome()
    } else {
        TvTrackerTheme {
            Scaffold(modifier = modifier) {
                WelcomeContent(
                    status,
                    viewModel.price.collectAsState().value,
                    viewModel::refresh,
                    viewModel::actionOk,
                    viewModel::buy
                )
            }
        }
    }
}

@Composable
fun WelcomeContent(
    status: WelcomeViewModel.Status,
    price: String,
    refresh: () -> Unit,
    actionOk: () -> Unit,
    buy: () -> Unit,
    pageIndex: Int = 0
) {
    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = pageIndex)
    val coroutineScope = rememberCoroutineScope()
    when (status) {
        WelcomeViewModel.Status.LoadingPrice -> {
            LoadingScreen()
        }

        WelcomeViewModel.Status.InitialisationError -> {
            ErrorScreen(refresh = refresh)
        }

        else -> {
            Column {
                Text(
                    text = "Your lifetime TV tracker for $price.".colored("lifetime"),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(sidePadding)
                )
                HorizontalPager(pagerState) { page ->
                    Box(Modifier.fillMaxSize().padding(horizontal = sidePadding)) {
                        if (page == 0) {
                            Screen1(next = {
                                coroutineScope.launch { pagerState.animateScrollToPage(1) }
                            })
                        } else {
                            Screen2(status, price, actionOk, buy)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Screen1(next: () -> Unit) {
    Column(Modifier.fillMaxHeight()) {
        Spacer(Modifier.height(32.dp))
        FeatureCard("No ads, no subscription, no trackers.".colored("ads"))
        Spacer(Modifier.height(16.dp))
        FeatureCard("No commitment: export your data.".colored("export"))
        Spacer(Modifier.height(16.dp))
        FeatureCard("Data backed up: email optional for recovery.".colored("recovery"))
        Spacer(Modifier.height(16.dp))
        FeatureCard("Made in London, UK and available on GitHub for free.".colored("free"))
        Spacer(Modifier.height(32.dp))
        Row(Modifier.align(Alignment.End), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "1/2",
                modifier = Modifier.weight(0.5f).padding(end = 8.dp),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall
            )
            Button(modifier = Modifier.weight(0.5f), onClick = next, shape = TvTrackerTheme.ShapeButton) {
                Text("Next")
                Spacer(Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, "")
            }
        }
    }
}

@Composable
private fun Screen2(status: WelcomeViewModel.Status, price: String, goToApp: () -> Unit, buy: () -> Unit) {
    Column(Modifier.fillMaxHeight()) {
        Spacer(Modifier.height(32.dp))
        FeatureCard("Notifications when episodes are released.".colored("Notifications"))
        Spacer(Modifier.height(16.dp))
        FeatureCard("Recommendations based on what you watch.".colored("you"))
        Spacer(Modifier.height(16.dp))
        FeatureCard("Search shows, movies, and people.".colored("Search"))
        Spacer(Modifier.height(32.dp))
        if (status == WelcomeViewModel.Status.InitialisationError) {
            Text(
                "Error setting up the app, either the server is broken or you're not connected to the internet.",
                color = MaterialTheme.colorScheme.error
            )
        } else {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(onClick = buy, modifier = Modifier.fillMaxWidth(0.5f), shape = TvTrackerTheme.ShapeButton) {
                    Text("Buy for $price")
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = goToApp,
                    shape = TvTrackerTheme.ShapeButton,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text("Pay later")
                    Spacer(Modifier.width(8.dp))
                    Box(contentAlignment = Alignment.Center) {
                        if (status == WelcomeViewModel.Status.Loading) {
                            LoadingIndicator(
                                modifier = Modifier.height(24.dp).aspectRatio(1f),
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowForward,
                                "ok",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(text: AnnotatedString) {
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Row {
            CoilImage(
                imageModel = { Res.drawable.ic_movie },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun String.colored(value: String): AnnotatedString {
    return buildAnnotatedString {
        append(this@colored)
        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
            ),
            start = this@colored.indexOf(value),
            end = this@colored.indexOf(value) + value.length
        )
    }
}
