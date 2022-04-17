package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.CollectEventFlow
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.ScaffoldColumnModifier
import com.myplaygroup.app.feature_login.presentation.destinations.ForgotPasswordScreenDestination
import com.myplaygroup.app.feature_login.presentation.login.components.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current


    val scaffoldState = CollectEventFlow(viewModel)
    var textFieldFocused by remember {
        mutableStateOf(false)
    }

    val isBusy = viewModel.isBusy.value
    val user = viewModel.state.username
    val password = viewModel.state.password

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = ScaffoldColumnModifier(){
                textFieldFocused = false
                focusManager.clearFocus()
            }
        ) {
            val widthModifier = Modifier
                .width(400.dp)
                .padding(horizontal = 30.dp)

            LoginImageAndText(
                imageSize = 200.dp,
                imageTextSpace = 30.dp,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            LoginTextFields(
                user = user,
                password = password,
                onUserChange = {
                    viewModel.onEvent(LoginEvent.EnteredUsername(it))
                },
                onPasswordChange = {
                    viewModel.onEvent(LoginEvent.EnteredPassword(it))
                },
                onFocusChange = {
                    textFieldFocused = it.isFocused
                },
                enabled = !isBusy,
                modifier = widthModifier
            )

            Spacer(modifier = Modifier.height(18.dp))

            LoginButton(
                enabled = !isBusy && user.isNotBlank() && password.isNotBlank(),
                loginEvent = {
                    textFieldFocused = false
                    focusManager.clearFocus()
                    viewModel.onEvent(LoginEvent.LoginTapped)
                },
                modifier = widthModifier
            )
            
            Spacer(modifier = Modifier.height(10.dp))

            LoginForgotPassword(
                isBusy = isBusy,
                forgotPasswordEvent = {
                    navigator.navigate(ForgotPasswordScreenDestination())
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