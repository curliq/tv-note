package com.free.tvtracker.ui.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import besttvtracker.composeapp.generated.resources.Res
import besttvtracker.composeapp.generated.resources.ic_code
import besttvtracker.composeapp.generated.resources.ic_customer_support
import besttvtracker.composeapp.generated.resources.ic_delete_account
import besttvtracker.composeapp.generated.resources.ic_settings_restore
import besttvtracker.composeapp.generated.resources.ic_tos
import com.free.tvtracker.expect.BuildVersion
import com.free.tvtracker.expect.OsPlatform
import com.free.tvtracker.ui.common.composables.ResImage
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import kotlinx.coroutines.flow.collectLatest

sealed class SettingsScreenNavAction {
    data object GoLogin : SettingsScreenNavAction()
    data object GoSignup : SettingsScreenNavAction()
    data class GoBrowser(val url: String) : SettingsScreenNavAction()
    data class EmailSupport(val email: String) : SettingsScreenNavAction()
}

object Constants {
    const val SUPPORT_EMAIL = "freetvtracker@proton.me"
    const val GITHUB_URL = "https://github.com/curliq/best-tv-tracker"
    const val APPLE_EULA_URL = "https://www.apple.com/legal/internet-services/itunes/dev/stdeula/"
}

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navAction: (SettingsScreenNavAction) -> Unit,
    paddingValues: PaddingValues
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.toaster.collectLatest {
            it?.let {
                snackbarHostState.showSnackbar(it)
            }
        }
    }
    val data = viewModel.data.collectAsState().value
    TvTrackerTheme {
        Scaffold(
            modifier = Modifier.padding(paddingValues),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        )
        {
            when (data) {
                SettingsUiState.Error -> {}
                SettingsUiState.Idle -> {}
                is SettingsUiState.Ok -> SettingsContent(data.data, snackbarHostState, navAction, viewModel::action)
            }
        }
    }
}

@Composable
fun SettingsContent(
    data: SettingsUiModel,
    snackbarHostState: SnackbarHostState,
    navAction: (SettingsScreenNavAction) -> Unit,
    action: (SettingsViewModel.Action) -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = TvTrackerTheme.sidePadding, horizontal = TvTrackerTheme.sidePadding)
        ) {
            Column(modifier = Modifier.padding(vertical = TvTrackerTheme.sidePadding)) {
                Column(modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)) {
                    Text(
                        "Your data",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { action(SettingsViewModel.Action.Export) }, shape = TvTrackerTheme.ShapeButton) {
                        Text("Export tracked content as csv")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                HorizontalDivider()
                AccountCard(data, navAction, action)
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Push notifications",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = {
                    action(SettingsViewModel.Action.TogglePushAllowed(data.pushNotificationEnabled))
                })
        ) {
            Row(modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePaddingHalf)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "New episode released",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = "Receive a push notification at 6pm GMT when a new episode " +
                            "is released of a show you track.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = data.pushNotificationEnabled,
                    onCheckedChange = { isChecked ->
                        action(SettingsViewModel.Action.TogglePushAllowed(isChecked))
                    }
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "App theme",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)
        )
        Spacer(Modifier.height(8.dp))
        Column(Modifier.selectableGroup()) {
            RadioBtn(
                data.theme == SettingsUiModel.Theme.System,
                "System default",
                { action(SettingsViewModel.Action.SetTheme(SettingsUiModel.Theme.System)) }
            )
            if (OsPlatform().get() == OsPlatform.Platform.Android) {
                RadioBtn(
                    data.theme == SettingsUiModel.Theme.Light,
                    "Light",
                    { action(SettingsViewModel.Action.SetTheme(SettingsUiModel.Theme.Light)) }
                )
                RadioBtn(
                    data.theme == SettingsUiModel.Theme.Dark,
                    "Dark",
                    { action(SettingsViewModel.Action.SetTheme(SettingsUiModel.Theme.Dark)) }
                )
            }
        }
        Spacer(Modifier.height(24.dp))

        Text(
            "App settings",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)
        )
        Spacer(Modifier.height(8.dp))
        TextButton(
            shape = TvTrackerTheme.ShapeButton,
            modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePaddingHalf).fillMaxWidth()
                .align(Alignment.Start),
            contentPadding = PaddingValues(TvTrackerTheme.sidePaddingHalf),
            onClick = { action(SettingsViewModel.Action.RestorePurchase) }
        ) {
            ResImage(Res.drawable.ic_settings_restore, "restore purchase", tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Text(text = "Restore purchase")
            Spacer(modifier = Modifier.weight(1f))
        }
        TextButton(
            shape = TvTrackerTheme.ShapeButton,
            modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePaddingHalf).fillMaxWidth()
                .align(Alignment.Start),
            contentPadding = PaddingValues(TvTrackerTheme.sidePaddingHalf),
            onClick = { navAction(SettingsScreenNavAction.EmailSupport(Constants.SUPPORT_EMAIL)) }
        ) {
            ResImage(Res.drawable.ic_customer_support, "email developer", tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Text(text = "Email developer")
            Spacer(modifier = Modifier.weight(1f))
        }
        TextButton(
            shape = TvTrackerTheme.ShapeButton,
            modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePaddingHalf).fillMaxWidth()
                .align(Alignment.Start),
            contentPadding = PaddingValues(TvTrackerTheme.sidePaddingHalf),
            onClick = { navAction(SettingsScreenNavAction.GoBrowser(Constants.GITHUB_URL)) },
        ) {
            ResImage(Res.drawable.ic_code, "restore", tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Text("github.com/curliq/best-tv-tracker")
            Spacer(modifier = Modifier.weight(1f))
        }
        if (OsPlatform().get() == OsPlatform.Platform.IOS) {
            TextButton(
                shape = TvTrackerTheme.ShapeButton,
                modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePaddingHalf).fillMaxWidth()
                    .align(Alignment.Start),
                contentPadding = PaddingValues(TvTrackerTheme.sidePaddingHalf),
                onClick = {
                    navAction(SettingsScreenNavAction.GoBrowser(Constants.APPLE_EULA_URL))
                },
            ) {
                ResImage(Res.drawable.ic_tos, "tos", tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("Terms of use (EULA)")
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        TextButton(
            shape = TvTrackerTheme.ShapeButton,
            modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePaddingHalf).fillMaxWidth()
                .align(Alignment.Start),
            contentPadding = PaddingValues(TvTrackerTheme.sidePaddingHalf),
            onClick = { action(SettingsViewModel.Action.Logout) }
        ) {
            ResImage(Res.drawable.ic_delete_account, "delete", tint = MaterialTheme.colorScheme.error)
            Spacer(Modifier.width(8.dp))
            Text(text = "Delete account", color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))
        var tapCount by remember { mutableStateOf(0) }

        // Trigger an action when tapCount reaches 10
        LaunchedEffect(tapCount) {
            if (tapCount >= 10) {
                action(SettingsViewModel.Action.EnableFreeApp)
                snackbarHostState.showSnackbar("Communism enabled")
                tapCount = 0
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Made in London, UK",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally).clickable { tapCount++ },
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Version ${BuildVersion().name()} - ${BuildVersion().code()}",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(24.dp))
    }

}

@Composable
private fun AccountCard(
    data: SettingsUiModel,
    navAction: (SettingsScreenNavAction) -> Unit,
    action: (SettingsViewModel.Action) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)) {
        Spacer(modifier = Modifier.height(24.dp))
        if (data.isAnon) {
            Text(
                text = "You have the option to create an account to backup your content on the cloud.",
                style = MaterialTheme.typography.bodySmall
            )
            Row(modifier = Modifier.padding(top = TvTrackerTheme.sidePadding)) {
                OutlinedButton(
                    onClick = { navAction(SettingsScreenNavAction.GoLogin) },
                    shape = TvTrackerTheme.ShapeButton,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(0.5f, true)
                ) {
                    Text("Log in")
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(
                    onClick = { navAction(SettingsScreenNavAction.GoSignup) },
                    shape = TvTrackerTheme.ShapeButton,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(0.5f, true)
                ) {
                    Text("Create account")
                }
            }
        } else {
            Text(
                text = "Logged in as: ${data.personalInfo?.username}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Email: ${data.personalInfo?.email ?: "n/a"}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(16.dp))
            val showLogoutConfirmation = remember { mutableStateOf(false) }

            Crossfade(targetState = showLogoutConfirmation.value) {
                if (it) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "".run {
                                if (OsPlatform().get() == OsPlatform.Platform.IOS)
                                    "The app will close."
                                else
                                    "Confirm log out?"
                            },
                            style = MaterialTheme.typography.labelLarge
                        )
                        Spacer(Modifier.width(8.dp))
                        TextButton(
                            shape = TvTrackerTheme.ShapeButton,
                            onClick = { showLogoutConfirmation.value = false }) {
                            Text(text = if (OsPlatform().get() == OsPlatform.Platform.IOS) "Cancel" else "No")
                        }
                        TextButton(
                            shape = TvTrackerTheme.ShapeButton,
                            onClick = { action(SettingsViewModel.Action.Logout) }) {
                            Text(
                                text = if (OsPlatform().get() == OsPlatform.Platform.IOS) "Confirm" else "Yes",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                } else {
                    Row {
                        OutlinedButton(
                            onClick = { showLogoutConfirmation.value = true },
                            shape = TvTrackerTheme.ShapeButton,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                        ) {
                            Text(text = "Log out")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RadioBtn(selected: Boolean, text: String, action: () -> Unit) {
    Row(
        Modifier.fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .selectable(
                selected = selected,
                onClick = { action() },
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // null recommended for accessibility with screen readers
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
