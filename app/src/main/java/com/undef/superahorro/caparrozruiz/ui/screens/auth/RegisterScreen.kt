package com.undef.superahorro.caparrozruiz.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.undef.superahorro.caparrozruiz.R
import com.undef.superahorro.caparrozruiz.ui.components.AppTextField
import com.undef.superahorro.caparrozruiz.ui.components.AuthContainer
import com.undef.superahorro.caparrozruiz.ui.components.PrimaryButton
import com.undef.superahorro.caparrozruiz.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AuthContainer {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = stringResource(R.string.auth_register_title), style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.dp))
            AppTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChanged,
                label = stringResource(R.string.auth_name_label)
            )
            Spacer(modifier = Modifier.height(12.dp))
            AppTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                label = stringResource(R.string.auth_email_label)
            )
            Spacer(modifier = Modifier.height(12.dp))
            AppTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = stringResource(R.string.auth_password_label)
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = stringResource(R.string.auth_register_button),
                loading = uiState.isLoading,
                onClick = { viewModel.register(onRegisterSuccess) }
            )
            TextButton(onClick = onBackToLogin) {
                Text(text = stringResource(R.string.auth_back_to_login_action))
            }
        }
    }
}
