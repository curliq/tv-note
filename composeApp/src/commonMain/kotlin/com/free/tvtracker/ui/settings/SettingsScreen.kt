package com.free.tvtracker.ui.settings

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.theme.TvTrackerTheme

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, paddingValues: PaddingValues) {
    val data = viewModel.data.collectAsState().value
    TvTrackerTheme {
        Scaffold(Modifier.padding(paddingValues)) {
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
    Column() {
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = TvTrackerTheme.sidePadding, horizontal = TvTrackerTheme.sidePadding)
        ) {
            Column(modifier = Modifier.padding(horizontal = TvTrackerTheme.sidePadding)) {
                Spacer(modifier = Modifier.height(TvTrackerTheme.sidePadding))
                Text(
                    "Backup your content",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
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
            onClick = null, // null recommended for accessibility with screenreaders
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
