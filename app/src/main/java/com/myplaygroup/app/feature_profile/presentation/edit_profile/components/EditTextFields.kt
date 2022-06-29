package com.myplaygroup.app.feature_profile.presentation.edit_profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.DropdownOutlinedTextField
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_profile.domain.model.EditProfileType
import com.myplaygroup.app.feature_profile.presentation.edit_profile.EditProfileState

@Composable
fun ColumnScope.EditTextFields(
    state: EditProfileState,
    isBusy: Boolean,
    enteredProfileName: (String) -> Unit = {},
    enteredPhoneNumber: (String) -> Unit = {},
    enteredPassword: (String) -> Unit = {},
    enteredRepeatedPassword: (String) -> Unit = {},
    dropdownChanged: (EditProfileType) -> Unit = {},
) {
    val widthModifier = Modifier
        .width(400.dp)
        .padding(horizontal = 30.dp)

    if(state.isAdmin){
        DropdownOutlinedTextField(
            label = "Profile Type",
            items = listOf(
                EditProfileType.PHONE_NUMBER.name,
                EditProfileType.PROFILE_NAME.name,
                EditProfileType.PASSWORD.name
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White
            ),
            selected = state.editProfileType.name,
            errorMessage = null,
            selectedChanged = {
                val profileType = EditProfileType.valueOf(it)
                dropdownChanged(profileType)
            },
            modifier = widthModifier
        )

        Spacer(modifier = Modifier.height(10.dp))
    }

    when(state.editProfileType){
        EditProfileType.PROFILE_NAME -> {
            ProfileField(
                value = state.profileName,
                enabled = !isBusy,
                placeholder = stringResource(id = R.string.profile_name_placeholder),
                label = stringResource(id = R.string.profile_name_label),
                onTextChange = enteredProfileName,
                modifier = widthModifier,
                errorMessage = state.profileNameError
            )
        }
        EditProfileType.PHONE_NUMBER -> {
            ProfileField(
                value = state.phoneNumber,
                enabled = !isBusy,
                placeholder = stringResource(id = R.string.phone_number_placeholder),
                label = stringResource(id = R.string.phone_number_label),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                onTextChange = enteredPhoneNumber,
                modifier = widthModifier,
                errorMessage = state.phoneNumberError
            )
        }
        EditProfileType.PASSWORD -> {
            ProfileField(
                value = state.password,
                enabled = !isBusy,
                placeholder = stringResource(id = R.string.password_placeholder),
                label = stringResource(id = R.string.password_label),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isPassword = true,
                onTextChange = enteredPassword,
                modifier = widthModifier,
                errorMessage = state.passwordError
            )

            ProfileField(
                value = state.repeatedPassword,
                enabled = !isBusy,
                placeholder = stringResource(id = R.string.confirm_password_placeholder),
                label = stringResource(id = R.string.confirm_password_label),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isPassword = true,
                onTextChange = enteredRepeatedPassword,
                modifier = widthModifier,
                errorMessage = state.repeatedPasswordError
            )
        }
        else -> {}
    }
}

@Preview
@Composable
fun EditProfileScreenPreview() {
    MyPlaygroupTheme {
        Column {
            EditTextFields(
                state = EditProfileState(
                    isAdmin = true,
                    editProfileType = EditProfileType.PASSWORD
                ),
                isBusy = false,
            )
        }
    }
}