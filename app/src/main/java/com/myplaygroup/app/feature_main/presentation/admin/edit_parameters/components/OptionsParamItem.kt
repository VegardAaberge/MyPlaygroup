package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import com.myplaygroup.app.core.presentation.components.DropdownOutlinedTextField
import com.myplaygroup.app.feature_main.domain.model.ParameterItem

@Composable
fun OptionsParamItem(
    item: ParameterItem,
    optionChanged: (Any, String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val options = (item.value as List<String>).toMutableList()

    Column {
        DropdownOutlinedTextField(
            label = "User",
            items = options.subList(1, options.size),
            selected = options.first(),
            isOutlined = false,
            enabled = item.enabled,
            errorMessage = item.error,
            selectedChanged = {
                focusManager.clearFocus()
                if(item.enabled){
                    options[0] = it
                    optionChanged(options, item.key)
                }
            }
        )
    }
}