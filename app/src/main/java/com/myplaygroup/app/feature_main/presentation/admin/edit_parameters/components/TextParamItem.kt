package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType.NUMBER
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType.STRING
import com.myplaygroup.app.feature_main.domain.model.ParameterItem

@Composable
fun TextParamItem(
    item: ParameterItem,
    valueChanged: (Any, String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = item.value.toString(),
        label = {
            Text(text = item.getTitle())
        },
        isError = item.error != null,
        onValueChange = {
            valueChanged(it, item.key)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if(item.type == NUMBER){
                KeyboardType.Number
            } else KeyboardType.Text
        ),
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
                STRING,
                key = "classData",
                value = "text"
            ),
        ){ value, key ->

        }
    }
}