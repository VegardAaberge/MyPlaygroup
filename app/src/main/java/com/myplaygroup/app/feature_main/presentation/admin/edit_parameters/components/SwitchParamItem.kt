package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType
import com.myplaygroup.app.feature_main.domain.model.ParameterItem

@Composable
fun SwitchParamItem(
    item: ParameterItem,
    switchChanged: (Any, String) -> Unit
) {
    val checked = item.value as Boolean

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .fillMaxWidth()
    ) {
        Text(text = item.getTitle())
        Switch(
            checked = checked,
            onCheckedChange = {
                switchChanged(it, item.key)
            }
        )
    }

    Divider(modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
fun SwitchParamItemPreview() {
    MyPlaygroupTheme {
        SwitchParamItem(
            item = ParameterItem(
                ParameterDisplayType.SWITCH,
                key = "Key",
                value = true
            ),
        ){ value, key ->

        }
    }
}