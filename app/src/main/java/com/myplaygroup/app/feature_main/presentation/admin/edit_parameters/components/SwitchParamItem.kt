package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.myplaygroup.app.feature_main.domain.model.ParameterItem

@Composable
fun SwitchParamItem(
    item: ParameterItem,
    switchChanged: (Boolean) -> Unit
) {
    val checked = item.value as Boolean

    Row {
        Text(text = item.key)
        Switch(
            checked = checked,
            onCheckedChange = {
                switchChanged(it)
            }
        )
    }
}