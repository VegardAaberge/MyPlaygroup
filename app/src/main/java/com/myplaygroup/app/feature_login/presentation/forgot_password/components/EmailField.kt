package com.myplaygroup.app.feature_login.presentation.forgot_password.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun EmailField(
    textValue: String,
    isEnabled: Boolean,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = textValue,
        placeholder = { Text(text = stringResource(id = R.string.forgot_password_placeholder)) },
        label = { Text(text = stringResource(id = R.string.forgot_password_label)) },
        singleLine = true,
        enabled = isEnabled,
        onValueChange = onTextChanged,
        modifier = modifier
    )
}