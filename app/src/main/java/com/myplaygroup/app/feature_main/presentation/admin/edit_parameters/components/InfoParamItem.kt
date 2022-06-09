package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.myplaygroup.app.feature_main.domain.model.ParameterItem

@Composable
fun InfoParamItem(
    item: ParameterItem
) {
    Row {
        Text(text = item.key)
        Text(text = item.value.toString())
    }
}