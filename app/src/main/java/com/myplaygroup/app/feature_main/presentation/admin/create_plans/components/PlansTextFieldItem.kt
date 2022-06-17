package com.myplaygroup.app.feature_main.presentation.admin.create_plans.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ColumnScope.PlansTextFieldItem(
    label: String,
    errorMessage: String?,
    selected: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    selectedChanged: (String) -> Unit
){
    OutlinedTextField(
        label = {
            Text(text = label)
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Create,
                contentDescription = null,
            )
        },
        isError = errorMessage != null,
        value = selected,
        keyboardOptions = keyboardOptions,
        onValueChange = selectedChanged,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
    )

    if(errorMessage != null){
        Text(
            text = errorMessage,
            color = MaterialTheme.colors.error,
            modifier = Modifier.align(Alignment.Start)
        )
    }
}