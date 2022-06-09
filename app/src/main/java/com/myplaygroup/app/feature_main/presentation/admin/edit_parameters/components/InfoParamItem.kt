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
    ReadonlyTextField(
        label = item.getTitle(),
        fieldValue = item.value.toString(),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
        },
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