package com.myplaygroup.app.feature_login.presentation.create_profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.CollectEventFlow
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.ScaffoldColumnModifier
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

    val scaffoldState = CollectEventFlow(viewModel, navigator)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.profile_screen_title),
                navigator = navigator!!,
            ){
                IconButton(
                    onClick = {
                        viewModel.onEvent(CreateProfileScreenEvent.SaveProfile)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Save Button",
                    )
                }
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = ScaffoldColumnModifier(){
                focusManager.clearFocus()
            }
        ) {
            ProfileImage(
                takePicture = {
                    viewModel.onEvent(CreateProfileScreenEvent.TakePicture)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextFields(viewModel)
        }
    }
}

@Composable
fun TextFields(
    viewModel: CreateProfileViewModel
) {
    val isBusy = viewModel.isBusy.value
    val profileName = viewModel.state.profileName
    val phoneNumber = viewModel.state.phoneNumber
    val email = viewModel.state.email
    val password = viewModel.state.password
    val repeatedPassword = viewModel.state.repeatedPassword

    val widthModifier = Modifier
        .width(400.dp)
        .padding(horizontal = 30.dp)

    ProfileField(
        value = profileName,
        enabled = !isBusy,
        placeholder = stringResource(id = R.string.profile_name_placeholder),
        label = stringResource(id = R.string.profile_name_label),
        onTextChange = {
            viewModel.onEvent(CreateProfileScreenEvent.EnteredProfileName(it))
        },
        modifier = widthModifier
    )

    ProfileField(
        value = phoneNumber,
        enabled = !isBusy,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        placeholder = stringResource(id = R.string.phone_number_placeholder),
        label = stringResource(id = R.string.phone_number_label),
        onTextChange = {
            viewModel.onEvent(CreateProfileScreenEvent.EnteredPhoneNumber(it))
        },
        modifier = widthModifier
    )

    ProfileField(
        value = email,
        enabled = !isBusy,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        placeholder = stringResource(id = R.string.email_placeholder),
        label = stringResource(id = R.string.email_label),
        onTextChange = {
            viewModel.onEvent(CreateProfileScreenEvent.EnteredEmail(it))
        },
        modifier = widthModifier
    )

    ProfileField(
        value = password,
        enabled = !isBusy,
        isPassword = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        placeholder = stringResource(id = R.string.password_placeholder),
        label = stringResource(id = R.string.password_label),
        onTextChange = {
            viewModel.onEvent(CreateProfileScreenEvent.EnteredPassword(it))
        },
        modifier = widthModifier
    )

    ProfileField(
        value = repeatedPassword,
        enabled = !isBusy,
        isPassword = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        placeholder = stringResource(id = R.string.confirm_password_placeholder),
        label = stringResource(id = R.string.confirm_password_label),
        onTextChange = {
            viewModel.onEvent(CreateProfileScreenEvent.EnteredRepeatedPassword(it))
        },
        modifier = widthModifier
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CreateProfileScreen()
}