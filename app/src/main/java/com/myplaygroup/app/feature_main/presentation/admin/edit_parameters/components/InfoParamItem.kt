package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.myplaygroup.app.core.presentation.components.ReadonlyTextField
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType
import com.myplaygroup.app.feature_main.domain.model.ParameterItem

@Composable
fun InfoParamItem(
    item: ParameterItem
) {
    val value = if(item.value is List<*>){
        item.value.map { x -> x.toString().lowercase().replaceFirstChar { y -> y.uppercase() } }.joinToString()
    }else item.value.toString().lowercase().replaceFirstChar { y -> y.uppercase() }.replace('_', ' ')

    ReadonlyTextField(
        label = item.getTitle(),
        fieldValue = value,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
        },
        isError = item.error != null,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = Color.LightGray,
        ),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Preview
@Composable
fun InfoParamItemPreview() {
    MyPlaygroupTheme {
        InfoParamItem(
            item = ParameterItem(
                ParameterDisplayType.INFO,
                key = "Key",
                value = "text"
            ),
        )
    }
}