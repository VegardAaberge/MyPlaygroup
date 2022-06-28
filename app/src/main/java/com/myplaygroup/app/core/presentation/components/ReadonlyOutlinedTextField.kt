package com.myplaygroup.app.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
fun ReadonlyOutlinedTextField(
    label: String,
    fieldValue: String,
    modifier : Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    trailingIcon: @Composable (() -> Unit)? = null,
    action: () -> Unit = {},
) {
    Box() {
        OutlinedTextField(
            value = fieldValue,
            onValueChange = {  },
            trailingIcon = trailingIcon,
            enabled = enabled,
            isError = isError,
            label = {
                Text(text = label)
            },
            colors = colors,
            modifier = modifier
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = {
                    action()
                }),
        )
    }
}