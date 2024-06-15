package com.free.tvtracker.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Text

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.theme.TvTrackerTheme

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val data = viewModel.data.collectAsState().value
    TvTrackerTheme {
        Scaffold {
            when (data) {
                SettingsUiState.Error -> {}
                SettingsUiState.Loading -> {}
                is SettingsUiState.Ok -> SettingsContent(data.data, viewModel::action)
            }
        }
    }
}

@Composable
fun SettingsContent(data: SettingsUiModel, action: (SettingsViewModel.Action) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)) {
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = TvTrackerTheme.sidePadding)) {
            Column(modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)) {
                Spacer(modifier = Modifier.height(TvTrackerTheme.sidePadding))
                Text("Backup your tracking", style = TvTrackerTheme.Typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "You have the option to use an account to backup all your tracked shows, " +
                        "none of your data is used for any purpose other than letting you restore it if needed",
                    style = TvTrackerTheme.Typography.bodySmall
                )
                Row(
                    modifier = Modifier.padding(
                        top = 24.dp,
                        bottom = TvTrackerTheme.sidePadding
                    )
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(0.5f, true)
                    ) {
                        Text("Log in")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(0.5f, true)
                    ) {
                        Text("Sign up")
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Text("Push notifications", style = TvTrackerTheme.Typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.clickable(
                onClick = {
                    action(SettingsViewModel.Action.TogglePushAllowed(data.pushNotificationEnabled))
                }
            )
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "New episode released",
                    style = TvTrackerTheme.Typography.bodyLarge,
                )
                Text(
                    text = "Receive a push notification at 6pm GMT when a new episode " +
                        "is released of a show you track",
                    style = TvTrackerTheme.Typography.bodySmall,
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
}
