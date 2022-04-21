package com.myplaygroup.app.feature_login.presentation.forgot_password.components

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.myplaygroup.app.R

@Composable
fun EmailField(
    email: String,
    isEnabled: Boolean,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = email,
        placeholder = { Text(text = stringResource(id = R.string.forgot_password_placeholder)) },
        label = { Text(text = stringResource(id = R.string.forgot_password_label)) },
        singleLine = true,
        enabled = isEnabled,
        onValueChange = onTextChanged,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        modifier = modifier
    )
}