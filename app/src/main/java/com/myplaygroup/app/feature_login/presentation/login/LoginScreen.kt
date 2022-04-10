package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.CollectEventFlow
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.ScaffoldColumnModifier
import com.myplaygroup.app.core.util.Resource
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
    val userResponse by viewModel.userResponse.observeAsState()

    val scaffoldState = CollectEventFlow(viewModel)
    var textFieldFocused by remember {
        mutableStateOf(false)
    }

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
                user = viewModel.user.value,
                password = viewModel.password.value,
                onUserChange = {
                    viewModel.onEvent(LoginEvent.EnteredUsername(it))
                },
                onPasswordChange = {
                    viewModel.onEvent(LoginEvent.EnteredPassword(it))
                },
                onFocusChange = {
                    textFieldFocused = it.isFocused
                },
                enabled = !(userResponse is Resource.Loading),
                modifier = widthModifier
            )

            Spacer(modifier = Modifier.height(18.dp))

            LoginButton(
                enabled = !(userResponse is Resource.Loading),
                loginEvent = {
                    textFieldFocused = false
                    focusManager.clearFocus()
                    viewModel.onEvent(LoginEvent.LoginTapped)
                },
                modifier = widthModifier
            )
            
            Spacer(modifier = Modifier.height(10.dp))

            LoginForgotPassword(
                isLoading = userResponse is Resource.Loading,
                forgotPasswordEvent = {
                    navigator.navigate(ForgotPasswordScreenDestination())
                },
                modifier = widthModifier
            )

            Spacer(modifier = Modifier.height(130.dp))
        }

        if(userResponse is Resource.Loading){
            CustomProgressIndicator()
        }
    }
}