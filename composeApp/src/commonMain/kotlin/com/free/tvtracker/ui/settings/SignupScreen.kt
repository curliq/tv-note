package com.free.tvtracker.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.free.tvtracker.ui.common.composables.LoadingIndicator
import com.free.tvtracker.ui.common.theme.TvTrackerTheme
import com.free.tvtracker.ui.settings.signup.SignupViewModel

sealed class SignupScreenAction {
    data object GoBack : SignupScreenAction()
}

@Composable
fun SignupScreen(viewModel: SignupViewModel, navAction: (SignupScreenAction) -> Unit, paddingValues: PaddingValues) {
    TvTrackerTheme {
        Scaffold(modifier = Modifier.padding(paddingValues)) {
            SignupContent(
                viewModel.result.collectAsState().value,
                viewModel::signup,
                navAction,
                viewModel::looksLikeEmail
            )
        }
    }
}

@Composable
fun SignupContent(
    result: SignupViewModel.Result,
    action: (SignupViewModel.SignupAction) -> Unit,
    navAction: (SignupScreenAction) -> Unit,
    emailRegex: (String) -> Boolean
) {
    if (result == SignupViewModel.Result.Success) {
        navAction(SignupScreenAction.GoBack)
    }
    Column(modifier = Modifier.padding(TvTrackerTheme.sidePadding).fillMaxSize()) {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        var hasUpdatedEmail by rememberSaveable { mutableStateOf(false) }

        var username by rememberSaveable { mutableStateOf("") }
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                if (!hasUpdatedEmail) {
                    if (emailRegex(it)) {
                        email = it
                    } else {
                        email = ""
                    }
                }
            },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                hasUpdatedEmail = true
            },
            colors = if (hasUpdatedEmail) OutlinedTextFieldDefaults.colors() else OutlinedTextFieldDefaults.colors()
                .copy(
                    focusedTextColor = MaterialTheme.colorScheme.outline,
                    unfocusedTextColor = MaterialTheme.colorScheme.outline
                ),
            label = { Text("Email (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                action(SignupViewModel.SignupAction(username, email, password))
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            if (result != SignupViewModel.Result.Loading) {
                Text("Create account")
            } else {
                LoadingIndicator(
                    modifier = Modifier.height(24.dp).aspectRatio(1f),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
