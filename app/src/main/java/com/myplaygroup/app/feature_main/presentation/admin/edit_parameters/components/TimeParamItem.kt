package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.myplaygroup.app.core.presentation.components.ReadonlyTextField
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType
import com.myplaygroup.app.feature_main.domain.model.ParameterItem
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimeParamItem(
    item: ParameterItem,
    timeChanged: (Any, String) -> Unit
) {
    val context = LocalContext.current

    val itemTime = item.value as LocalTime
    val time = itemTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    val timePickerDialog = TimePickerDialog(
        context,
        {_, hour : Int, minute: Int ->
            timeChanged(LocalTime.of(hour, minute), item.key)
        }, itemTime.hour, itemTime.minute, false
    )

    ReadonlyTextField(
        label = item.getTitle(),
        fieldValue = time,
        isError = item.error != null,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = Color.LightGray,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        timePickerDialog.show()
    }
}

@Preview
@Composable
fun TimeParamItemPreview() {
    MyPlaygroupTheme {
        TimeParamItem(
            item = ParameterItem(
                ParameterDisplayType.TIME,
                key = "Key",
                value = LocalTime.of(12, 11)
            ),
        ){ value, key ->

        }
    }
}