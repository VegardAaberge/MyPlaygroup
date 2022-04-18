package com.myplaygroup.app.feature_login.presentation.create_profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.CollectEventFlow
import com.myplaygroup.app.core.presentation.components.ScaffoldColumnModifier
import com.myplaygroup.app.feature_login.presentation.create_profile.components.PasswordField
import com.myplaygroup.app.feature_login.presentation.create_profile.components.ProfileField
import com.myplaygroup.app.feature_login.presentation.create_profile.components.ProfileImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun CreateProfileScreen(
    navigator: DestinationsNavigator? = null,
    viewModel: CreateProfileViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    val isBusy = viewModel.isBusy.value
    val profileName = viewModel.state.profileName
    val phoneNumber = viewModel.state.phoneNumber
    val password = viewModel.state.password
    val repatedPassword = viewModel.state.repatedPassword

    val scaffoldState = CollectEventFlow(viewModel, navigator)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Edit Profile")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back Button",
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Save Button",
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                elevation = 12.dp
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = ScaffoldColumnModifier(){
                focusManager.clearFocus()
            }
        ) {
            val widthModifier = Modifier
                .width(400.dp)
                .padding(horizontal = 30.dp)

            ProfileImage()

            Spacer(modifier = Modifier.height(20.dp))

            ProfileField(
                value = profileName,
                enabled = !isBusy,
                placeholder = stringResource(id = R.string.profile_name_placeholder),
                label = stringResource(id = R.string.profile_name_label),
                onProfileNameChange = {
                    viewModel.onEvent(CreateProfileScreenEvent.EnteredProfileName(it))
                },
                modifier = widthModifier
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileField(
                value = phoneNumber,
                enabled = !isBusy,
                placeholder = stringResource(id = R.string.phone_number_placeholder),
                label = stringResource(id = R.string.phone_number_label),
                onProfileNameChange = {
                    viewModel.onEvent(CreateProfileScreenEvent.EnteredPhoneNumber(it))
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
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CreateProfileScreen()
}