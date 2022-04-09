package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.feature_login.presentation.login.components.LoginButton
import com.myplaygroup.app.feature_login.presentation.login.components.LoginTextFields
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LoginTextFields(
            user = viewModel.user.value,
            password = viewModel.password.value,
            onUserChange = {
                viewModel.onEvent(LoginEvent.EnteredUsername(it))
            },
            onPasswordChange = {
                viewModel.onEvent(LoginEvent.EnteredPassword(it))
            },
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(18.dp))
        
        LoginButton(loginEvent = {
            viewModel.onEvent(LoginEvent.LoginTapped)
        })
    }
}