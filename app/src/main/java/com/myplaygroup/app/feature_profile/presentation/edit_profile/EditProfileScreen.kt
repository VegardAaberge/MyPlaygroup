package com.myplaygroup.app.feature_profile.presentation.edit_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.app_bar.AppBarBackButton
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.core.presentation.components.scaffoldColumnModifier
import com.myplaygroup.app.feature_profile.domain.model.EditProfileType
import com.myplaygroup.app.feature_profile.presentation.edit_profile.components.EditTextFields
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination
@Composable
fun EditProfileScreen(
    editProfileType: String,
    navigator: DestinationsNavigator?,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val isBusy = viewModel.isBusy
    val isFilledIn = viewModel.state.isFilledIn()
    val scaffoldState = collectEventFlow(viewModel, navigator)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar(
                title = when(viewModel.state.editProfileType){
                    EditProfileType.PROFILE_NAME -> {
                        "Edit Profile Name"
                    }
                    EditProfileType.PHONE_NUMBER -> {
                        "Edit Phone Number"
                    }
                    EditProfileType.PASSWORD -> {
                        "Edit Password"
                    }
                    else -> {
                        "Edit Profile"
                    }
                },
                navigationIcon = {
                    AppBarBackButton(navigator!!)
                }
            ){
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
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
            EditTextFields(
                state = viewModel.state,
                isBusy = isBusy,
                enteredProfileName = {
                    viewModel.onEvent(EditProfileScreenEvent.EnteredProfileName(it))
                },
                enteredPhoneNumber = {
                    viewModel.onEvent(EditProfileScreenEvent.EnteredPhoneNumber(it))
                },
                enteredPassword = {
                    viewModel.onEvent(EditProfileScreenEvent.EnteredPassword(it))
                },
                enteredRepeatedPassword = {
                    viewModel.onEvent(EditProfileScreenEvent.EnteredRepeatedPassword(it))
                },
                dropdownChanged = {
                    viewModel.onEvent(EditProfileScreenEvent.DropdownChanged(it))
                }
            )
        }

        if(isBusy){
            CustomProgressIndicator()
        }
    }
}