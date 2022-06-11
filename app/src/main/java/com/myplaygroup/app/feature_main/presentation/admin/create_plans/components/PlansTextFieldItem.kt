package com.myplaygroup.app.feature_main.presentation.admin.create_plans.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlansTextFieldItem(
    label: String,
    selected: String,
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
        value = selected,
        onValueChange = selectedChanged,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
    )
}