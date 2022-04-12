package com.myplaygroup.app.feature_login.presentation.forgot_password

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.CollectEventFlow
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.ScaffoldColumnModifier
import com.myplaygroup.app.feature_login.presentation.forgot_password.components.EmailField
import com.myplaygroup.app.feature_login.presentation.forgot_password.components.ForgotPasswordInfo
import com.myplaygroup.app.feature_login.presentation.forgot_password.components.InsertCodeField
import com.myplaygroup.app.feature_login.presentation.forgot_password.components.SendActionIcon
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val scaffoldState = CollectEventFlow(viewModel)

    val isBusy = viewModel.isBusy.value
    val email = viewModel.state.email
    val code = viewModel.state.code
    val countDown = viewModel.state.countDown

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            SendActionIcon(){
                viewModel.onEvent(ForgotPasswordEvent.ResetPassword)
                focusManager.clearFocus()
            }
        }
    ) {
        val widthModifier = Modifier
            .width(400.dp)
            .padding(horizontal = 30.dp)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = ScaffoldColumnModifier(){
                focusManager.clearFocus()
            }
        ) {
            ForgotPasswordInfo(
                widthModifier = widthModifier,
                imageSize = 200.dp,
                headerFontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            EmailField(
                email = email,
                onTextChanged = {
                    viewModel.onEvent(ForgotPasswordEvent.EnteredEmail(it))
                },
                isEnabled = !isBusy,
                modifier = widthModifier
            )

            Spacer(modifier = Modifier.height(15.dp))

            InsertCodeField(
                code = code,
                countDown = countDown,
                onValueChange = {
                    viewModel.onEvent(ForgotPasswordEvent.EnteredCode(it))
                },
                modifier = widthModifier
            )
        }

        if(isBusy){
            CustomProgressIndicator()
        }
    }
}