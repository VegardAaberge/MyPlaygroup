package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import com.myplaygroup.app.feature_main.domain.model.ParameterItem

@Composable
fun TextParamItem(
    item: ParameterItem,
    textChanged: (String) -> Unit
) {
    TextField(
        value = item.value.toString(),
        onValueChange = {
            textChanged(it)
        }
    )
}