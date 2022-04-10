package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.components.CustomProgressIndicator
import com.myplaygroup.app.feature_login.presentation.login.components.LoginButton
import com.myplaygroup.app.feature_login.presentation.login.components.LoginTextFields
import com.myplaygroup.app.core.util.Resource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()

    val bottomHeight = animateDpAsState(
        targetValue = if(textFieldFocused){
            20.dp
        }else 150.dp,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 100
        )
    )

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

    val scaffold = Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        textFieldFocused = false
                        focusManager.clearFocus()
                    }
                )
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
                onFocusChange = {
                    textFieldFocused = it.isFocused
                },
                enabled = !(userResponse is Resource.Loading),
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(18.dp))

            LoginButton(
                enabled = !(userResponse is Resource.Loading),
                loginEvent = {
                    textFieldFocused = false
                    focusManager.clearFocus()
                    viewModel.onEvent(LoginEvent.LoginTapped)
                }
            )

            Spacer(modifier = Modifier.height(bottomHeight.value))
        }

        if(userResponse is Resource.Loading){
            CustomProgressIndicator()
        }
    }
}