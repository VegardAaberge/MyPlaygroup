package com.myplaygroup.app.feature_login.presentation.create_profile.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun ProfileField(
    value: String,
    enabled: Boolean,
    placeholder: String,
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isPassword: Boolean = false,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
)
{
    OutlinedTextField(
        value = value,
        placeholder = { Text(text = placeholder) },
        label = { Text(text = label) },
        enabled = enabled,
        singleLine = true,
        onValueChange = onTextChange,
        visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        modifier = modifier,
    )

    Spacer(modifier = Modifier.height(10.dp))
}