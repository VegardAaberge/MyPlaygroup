package com.myplaygroup.app.feature_login.presentation.create_profile.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.widget.Placeholder
import com.myplaygroup.app.R

@Composable
fun ProfileField(
    value: String,
    enabled: Boolean,
    placeholder: String,
    label: String,
    onProfileNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
)
{
    OutlinedTextField(
        value = value,
        placeholder = { Text(text = placeholder) },
        label = { Text(text = label) },
        enabled = enabled,
        singleLine = true,
        onValueChange = onProfileNameChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        modifier = modifier,
    )
}