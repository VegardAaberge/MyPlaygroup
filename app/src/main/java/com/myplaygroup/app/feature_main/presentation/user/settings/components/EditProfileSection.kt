package com.myplaygroup.app.feature_main.presentation.user.settings.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.components.ReadonlyTextField
import com.myplaygroup.app.feature_profile.domain.model.EditProfileType

@Composable
fun EditProfileSection(
    profileName: String,
    phoneNumber: String,
    editProfileData: (EditProfileType) -> Unit,
    seeBalance: () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))

    Divider(modifier = Modifier.fillMaxWidth())

    ReadonlyTextField(
        label = "Balance",
        fieldValue = "¥755",
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = Color.LightGray,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        seeBalance()
    }

    ReadonlyTextField(
        label = "Profile Name",
        fieldValue = profileName,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = Color.LightGray,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        editProfileData(EditProfileType.PROFILE_NAME)
    }

    ReadonlyTextField(
        label = "Phone Number",
        fieldValue = phoneNumber,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = Color.LightGray,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        editProfileData(EditProfileType.PHONE_NUMBER)
    }

    ReadonlyTextField(
        label = "Change Password",
        fieldValue = "",
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = Color.LightGray,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        editProfileData(EditProfileType.PASSWORD)
    }
}