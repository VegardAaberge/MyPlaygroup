package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.RequestPermissions
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.core.presentation.components.scaffoldColumnModifier
import com.myplaygroup.app.feature_login.presentation.login.components.LoginButton
import com.myplaygroup.app.feature_login.presentation.login.components.LoginImageAndText
import com.myplaygroup.app.feature_login.presentation.login.components.LoginTextFields
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator? = null,
    viewModel: LoginViewModel = hiltViewModel()
) {
    RequestPermissions()

    val focusManager = LocalFocusManager.current
    val scaffoldState = collectEventFlow(viewModel, navigator)

    val isBusy = viewModel.isBusy
    val user = viewModel.state.username
    val password = viewModel.state.password

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = scaffoldColumnModifier {
                focusManager.clearFocus()
            }
        ) {
            val widthModifier = Modifier
                .width(400.dp)
                .padding(horizontal = 30.dp)

            LoginImageAndText(
                imageSize = 240.dp,
                imageTextSpace = 30.dp,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            LoginTextFields(
                user = user,
                password = password,
                onUserChange = {
                    viewModel.onEvent(LoginScreenEvent.EnteredUsername(it))
                },
                onPasswordChange = {
                    viewModel.onEvent(LoginScreenEvent.EnteredPassword(it))
                },
                enabled = !isBusy,
                modifier = widthModifier
            )

            Spacer(modifier = Modifier.height(18.dp))

            LoginButton(
                enabled = !isBusy && user.isNotBlank() && password.isNotBlank(),
                loginEvent = {
                    focusManager.clearFocus()
                    viewModel.onEvent(LoginScreenEvent.LoginTapped)
                },
                modifier = widthModifier
            )

            Spacer(modifier = Modifier.height(130.dp))
        }

        if(isBusy){
            CustomProgressIndicator()
        }
    }
}