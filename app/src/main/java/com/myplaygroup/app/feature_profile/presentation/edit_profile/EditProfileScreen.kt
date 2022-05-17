package com.myplaygroup.app.feature_profile.presentation.edit_profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import com.myplaygroup.app.core.presentation.TopAppBar.AppBarBackButton
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.core.presentation.components.scaffoldColumnModifier
import com.myplaygroup.app.feature_profile.presentation.create_profile.components.ProfileField
import com.myplaygroup.app.feature_profile.presentation.create_profile.components.ProfileImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun EditProfileScreen(
    navigator: DestinationsNavigator?,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val isBusy = viewModel.isBusy.value
    val isFilledIn = viewModel.state.isFilledIn()
    val scaffoldState = collectEventFlow(viewModel, navigator)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.profile_screen_title),
                navigationIcon = {
                    AppBarBackButton(navigator!!)
                }
            ){
                IconButton(
                    onClick = {
                        viewModel.onEvent(EditProfileScreenEvent.SaveProfile)
                    },
                    enabled = !isBusy && isFilledIn,
                ) {
                    Text(text = "Save")
                }
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = scaffoldColumnModifier {
                focusManager.clearFocus()
            }
        ) {
            TextFields(viewModel)
        }

        if(isBusy){
            CustomProgressIndicator()
        }
    }
}

@Composable
fun TextFields(
    viewModel: EditProfileViewModel
) {
    val isBusy = viewModel.isBusy.value
    val profileName = viewModel.state.profileName
    val phoneNumber = viewModel.state.phoneNumber
    val email = viewModel.state.email

    val widthModifier = Modifier
        .width(400.dp)
        .padding(horizontal = 30.dp)

    ProfileField(
        value = profileName,
        enabled = !isBusy,
        placeholder = stringResource(id = R.string.profile_name_placeholder),
        label = stringResource(id = R.string.profile_name_label),
        onTextChange = {
            viewModel.onEvent(EditProfileScreenEvent.EnteredProfileName(it))
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
            viewModel.onEvent(EditProfileScreenEvent.EnteredPhoneNumber(it))
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
            viewModel.onEvent(EditProfileScreenEvent.EnteredEmail(it))
        },
        modifier = widthModifier
    )
}