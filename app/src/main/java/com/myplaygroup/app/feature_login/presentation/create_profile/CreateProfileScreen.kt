package com.myplaygroup.app.feature_login.presentation.create_profile

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.CollectEventFlow
import com.myplaygroup.app.feature_login.presentation.create_profile.components.PasswordField
import com.myplaygroup.app.feature_login.presentation.create_profile.components.ProfileField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun CreateProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: CreateProfileViewModel = hiltViewModel()
) {
    val isBusy = viewModel.isBusy.value
    val profileName = viewModel.state.profileName
    val password = viewModel.state.password
    val repatedPassword = viewModel.state.repatedPassword

    CollectEventFlow(viewModel, navigator)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val widthModifier = Modifier
            .width(400.dp)
            .padding(horizontal = 30.dp)

        ProfileField(
            profileName = profileName,
            enabled = !isBusy,
            onProfileNameChange = {
                viewModel.onEvent(CreateProfileScreenEvent.EnteredProfileName(it))
            },
            modifier = widthModifier
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordField(
            password = password,
            enabled = !isBusy,
            onPasswordChange = {
                viewModel.onEvent(CreateProfileScreenEvent.EnteredPassword(it))
            },
            modifier = widthModifier
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordField(
            password = repatedPassword,
            enabled = !isBusy,
            onPasswordChange = {
                viewModel.onEvent(CreateProfileScreenEvent.EnteredRepeatedPassword(it))
            },
            modifier = widthModifier
        )
    }
}