package com.myplaygroup.app.feature_login.presentation.forgot_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.myplaygroup.app.core.util.Resource
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.feature_login.presentation.forgot_password.components.EmailField
import com.myplaygroup.app.ui.theme.MyPlaygroupTheme
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.presentation.components.CollectEventFlow
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest


@Destination
@Composable
fun ForgotPasswoordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val scaffoldState = CollectEventFlow(viewModel)

    val emailResponse = viewModel.emailResponse.observeAsState().value

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is BaseViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = stringResource(id = R.string.send_email))
                },
                onClick = {
                    viewModel.onEvent(ForgotPasswordEvent.ResetPassword)
                    focusManager.clearFocus()
                },
                modifier = Modifier,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Add note"
                    )
                },
                shape = CircleShape,
                contentColor = Color.White,
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) {
        val widthModifier = Modifier
            .width(400.dp)
            .padding(horizontal = 30.dp)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
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
                        focusManager.clearFocus()
                    }
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.lock_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                fontSize = 30.sp,
                text = stringResource(id = R.string.forgot_password_header),
                modifier = widthModifier,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.forgot_password_explanation),
                modifier = widthModifier
            )

            Spacer(modifier = Modifier.height(20.dp))

            EmailField(
                textValue = viewModel.email.value,
                onTextChanged = {
                    viewModel.onEvent(ForgotPasswordEvent.EnteredEmail(it))
                },
                isEnabled = !(emailResponse is Resource.Loading),
                modifier = widthModifier
            )
        }

        if(emailResponse is Resource.Loading){
            CustomProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyPlaygroupTheme {
        ForgotPasswoordScreen()
    }
}