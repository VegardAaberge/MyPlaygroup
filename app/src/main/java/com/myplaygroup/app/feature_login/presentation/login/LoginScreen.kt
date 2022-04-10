package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.components.CustomProgressIndicator
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_login.presentation.destinations.ForgotPasswoordScreenDestination
import com.myplaygroup.app.feature_login.presentation.login.components.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Destination(start = true)
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val userResponse by viewModel.userResponse.observeAsState()

    val scaffoldState = rememberScaffoldState()
    var textFieldFocused by remember {
        mutableStateOf(false)
    }

    val fieldWidth = 400.dp
    val fieldPadding = 30.dp

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is LoginViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFAAFFFF),
                            Color(0xFFEEFFFF),
                        )
                    )
                )
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        textFieldFocused = false
                        focusManager.clearFocus()
                    }
                )
        ) {
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
                textFieldWidth = fieldWidth,
                textFieldHorizontalPadding = fieldPadding,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(18.dp))

            LoginButton(
                enabled = !(userResponse is Resource.Loading),
                btnWidth = fieldWidth,
                btnHorizontalPadding = fieldPadding,
                loginEvent = {
                    textFieldFocused = false
                    focusManager.clearFocus()
                    viewModel.onEvent(LoginEvent.LoginTapped)
                }
            )
            
            Spacer(modifier = Modifier.height(10.dp))

            LoginForgotPassword(
                isLoading = userResponse is Resource.Loading,
                forgotPasswordWidth = fieldWidth,
                forgotPasswordPadding = fieldPadding,
                forgotPasswordEvent = {
                    navigator.navigate(ForgotPasswoordScreenDestination())
                }
            )

            Spacer(modifier = Modifier.height(130.dp))
        }

        if(userResponse is Resource.Loading){
            CustomProgressIndicator()
        }
    }
}