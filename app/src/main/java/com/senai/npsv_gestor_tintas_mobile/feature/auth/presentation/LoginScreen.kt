package com.senai.npsv_gestor_tintas_mobile.feature.auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.senai.npsv_gestor_tintas_mobile.R
import com.senai.npsv_gestor_tintas_mobile.core.state.UiState

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit
) {

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginState by viewModel.loginState.collectAsState()


    LaunchedEffect(loginState) {
        if (loginState is UiState.Success) {
            onNavigateToHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.login_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(id = R.string.login_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(48.dp))


        OutlinedTextField(
            value = email,
            onValueChange = viewModel::onEmailChanged,
            label = { Text(stringResource(id = R.string.login_email_label)) },
            placeholder = { Text(stringResource(id = R.string.login_email_hint)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            value = password,
            onValueChange = viewModel::onPasswordChanged,
            label = { Text(stringResource(id = R.string.login_password_label)) },
            placeholder = { Text(stringResource(id = R.string.login_password_hint)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        TextButton(
            onClick = { /* TODO: Fluxo de esquecer senha */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(id = R.string.login_forgot_password))
        }

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = viewModel::login,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),

            enabled = loginState !is UiState.Loading
        ) {
            if (loginState is UiState.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(stringResource(id = R.string.login_button_enter))
            }
        }


        if (loginState is UiState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (loginState as UiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.weight(1f))


        TextButton(onClick = { /* TODO: Fluxo de solicitar cadastro */ }) {
            Text(stringResource(id = R.string.login_no_account))
        }
    }
}