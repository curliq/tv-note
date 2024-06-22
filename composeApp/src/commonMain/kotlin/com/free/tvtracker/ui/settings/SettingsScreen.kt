package com.free.tvtracker.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import besttvtracker.composeapp.generated.resources.Res
import besttvtracker.composeapp.generated.resources.bmc_full_logo
import com.free.tvtracker.ui.common.composables.ResImage
import com.free.tvtracker.ui.common.theme.TvTrackerTheme

sealed class SettingsScreenNavAction {
    data object GoLogin : SettingsScreenNavAction()
    data object GoSignup : SettingsScreenNavAction()
    data class GoBrowser(val url: String) : SettingsScreenNavAction()
}

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navAction: (SettingsScreenNavAction) -> Unit,
    paddingValues: PaddingValues
) {
    val data = viewModel.data.collectAsState().value
    TvTrackerTheme {
        Scaffold(Modifier.padding(paddingValues)) {
            when (data) {
                SettingsUiState.Error -> {}
                SettingsUiState.Idle -> {}
                is SettingsUiState.Ok -> SettingsContent(data.data, navAction, viewModel::action)
            }
        }
    }
}

@Composable
fun SettingsContent(
    data: SettingsUiModel,
    navAction: (SettingsScreenNavAction) -> Unit,
    action: (SettingsViewModel.Action) -> Unit
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = TvTrackerTheme.sidePadding, horizontal = TvTrackerTheme.sidePadding)
        ) {
            Spacer(modifier = Modifier.height(TvTrackerTheme.sidePadding))
            Column(modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)) {
                Text(
                    "Backup your content",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (data.isAnon) {
                    Text(
                        text = "You have the option to use an account to backup all your tracked content, " +
                            "none of your data is used for any purpose other than letting you restore it when needed, " +
                            "and no email is required",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(
                        modifier = Modifier.padding(
                            top = 24.dp,
                            bottom = TvTrackerTheme.sidePadding
                        )
                    ) {
                        Button(
                            onClick = { navAction(SettingsScreenNavAction.GoLogin) },
                            modifier = Modifier.weight(0.5f, true)
                        ) {
                            Text("Log in")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = { navAction(SettingsScreenNavAction.GoSignup) },
                            modifier = Modifier.weight(0.5f, true)
                        ) {
                            Text("Create account")
                        }
                    }
                } else {
                    Text(
                        text = "Logged in as ${data.personalInfo?.username}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Email: ${data.personalInfo?.email ?: "n/a"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(16.dp))
                    val showLogoutConfirmation = remember { mutableStateOf(false) }
                    if (showLogoutConfirmation.value) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(Modifier.weight(1f))
                            Text(text = "Log out?")
                            Spacer(Modifier.width(16.dp))
                            TextButton(onClick = { showLogoutConfirmation.value = false }) {
                                Text(text = "No")
                            }
                            TextButton(onClick = { action(SettingsViewModel.Action.Logout) }) {
                                Text(text = "Yes", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    } else {
                        Row {
                            Spacer(Modifier.weight(1f))
                            TextButton(onClick = {}) {
                                Text(text = "Contact support")
                            }
                            Spacer(Modifier.width(8.dp))
                            TextButton(onClick = { showLogoutConfirmation.value = true }) {
                                Text(text = "Log out", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
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
                            "is released of a show you track",
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
        Spacer(Modifier.height(24.dp))
        Text(
            "About",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)
        )
        Spacer(Modifier.height(8.dp))
        Row(Modifier.padding(horizontal = TvTrackerTheme.sidePadding), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { navAction(SettingsScreenNavAction.GoBrowser("https://buymeacoffee.com/freetvtracker")) }) {
                Text(text = "Donate")
            }
            Spacer(Modifier.width(16.dp))
            Text("Powered by ", style = MaterialTheme.typography.labelSmall)
            val tint = if (isSystemInDarkTheme()) {
                //todo test on ios
                Color(0xffeb4034)
            } else {
                null
            }
            ResImage(
                Res.drawable.bmc_full_logo,
                contentDescription = "justwatch",
                modifier = Modifier.height(24.dp),
                tint = tint
            )
        }
        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))
        TextButton(
            onClick = { navAction(SettingsScreenNavAction.GoBrowser("https://github.com/curliq/best-tv-tracker")) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("github.com/curliq/best-tv-tracker")
        }
        Box(
            Modifier.size(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.outlineVariant)
                .align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Made in East London",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(24.dp))
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
