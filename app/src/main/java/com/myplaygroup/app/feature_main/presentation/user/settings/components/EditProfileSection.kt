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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.ReadonlyTextField
import com.myplaygroup.app.feature_profile.domain.model.EditProfileType
import kotlin.math.absoluteValue

@Composable
fun EditProfileSection(
    profileName: String,
    phoneNumber: String,
    balance: Long,
    editProfileData: (EditProfileType) -> Unit,
    seeBalance: () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))

    Divider(modifier = Modifier.fillMaxWidth())

    ReadonlyTextField(
        label = stringResource(R.string.settings_balance),
        fieldValue = if(balance < 0) "-¥${balance.absoluteValue}" else "¥$balance",
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
        label = stringResource(R.string.settings_profile_name),
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
        label = stringResource(R.string.settings_phone_number),
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
        label = stringResource(R.string.settings_change_password),
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