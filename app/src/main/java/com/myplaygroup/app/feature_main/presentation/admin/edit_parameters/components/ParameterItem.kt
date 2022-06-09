package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.myplaygroup.app.feature_main.domain.model.EditItem

@Composable
fun ParameterItem(
    parameterItems: EditItem
) {
    Column {
        Text(text = parameterItems.key)
        Text(text = parameterItems.type)
        Text(text = parameterItems.value)
    }
}