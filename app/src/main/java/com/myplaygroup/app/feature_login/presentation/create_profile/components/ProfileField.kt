package com.myplaygroup.app.feature_login.presentation.create_profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R

@Composable
fun ProfileField(
    profileName: String,
    enabled: Boolean,
    onProfileNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
)
{
    OutlinedTextField(
        value = profileName,
        placeholder = { Text(text = stringResource(id = R.string.profile_name_placeholder)) },
        label = { Text(text = stringResource(id = R.string.profile_name_label)) },
        enabled = enabled,
        singleLine = true,
        onValueChange = onProfileNameChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        modifier = modifier,
    )
}