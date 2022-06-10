package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType
import com.myplaygroup.app.feature_main.domain.model.ParameterItem

@Composable
fun TextParamItem(
    item: ParameterItem,
    valueChanged: (Any, String) -> Unit
) {
    TextField(
        value = item.value.toString(),
        label = {
            Text(text = item.getTitle())
        },
        isError = item.error != null,
        onValueChange = {
            valueChanged(it, item.key)
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Edit,
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
fun TextParamItemPreview() {
    MyPlaygroupTheme {
        TextParamItem(
            item = ParameterItem(
                ParameterDisplayType.STRING,
                key = "classData",
                value = "text"
            ),
        ){ value, key ->

        }
    }
}