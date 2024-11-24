package com.free.tvtracker.ui.settings.login

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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.free.tvtracker.expect.OsPlatform
import com.free.tvtracker.ui.common.composables.LoadingIndicator
import com.free.tvtracker.ui.common.theme.TvTrackerTheme

sealed class LoginScreenNavAction {
    data object GoBack : LoginScreenNavAction()
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navAction: (LoginScreenNavAction) -> Unit,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    TvTrackerTheme {
        Scaffold(modifier = Modifier.padding(paddingValues)) {
            LoginContent(viewModel.result.collectAsState().value, navAction, viewModel::login)
        }
    }
}

@Composable
fun LoginContent(
    result: LoginViewModel.Result,
    navAction: (LoginScreenNavAction) -> Unit,
    action: (LoginViewModel.LoginAction) -> Unit
) {
    if (result == LoginViewModel.Result.Success) {
        navAction(LoginScreenNavAction.GoBack)
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (OsPlatform().get() == OsPlatform.Platform.Android)
            focusRequester.requestFocus()
    }

    Column(modifier = Modifier.padding(TvTrackerTheme.sidePadding).fillMaxSize()) {
        var username by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
        )
        Spacer(Modifier.height(8.dp))
        var password by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                action(LoginViewModel.LoginAction(username, password))
            },
            modifier = Modifier.align(Alignment.End).fillMaxWidth(0.5f)
        ) {
            val isLoading = result == LoginViewModel.Result.Loading
            if (isLoading) {
                LoadingIndicator(
                    modifier = Modifier.height(24.dp).aspectRatio(1f),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Login", modifier = Modifier.alpha(if (isLoading) 0f else 1f))
            }
        }
        if (result == LoginViewModel.Result.Error) {
            Spacer(Modifier.height(16.dp))
            Text("Something went wrong, please try again", color = MaterialTheme.colorScheme.error)
        }
    }
}
