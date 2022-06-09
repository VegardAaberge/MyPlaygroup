package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.myplaygroup.app.core.presentation.components.ReadonlyTextField
import com.myplaygroup.app.feature_main.domain.model.ParameterItem
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimeParamItem(
    item: ParameterItem,
    timeChanged: (LocalTime) -> Unit
) {
    val context = LocalContext.current

    val itemTime = item.value as LocalTime
    val time = itemTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    val timePickerDialog = TimePickerDialog(
        context,
        {_, hour : Int, minute: Int ->
            timeChanged(LocalTime.of(hour, minute))
        }, itemTime.hour, itemTime.minute, false
    )

    ReadonlyTextField(label = item.key, fieldValue = time) {
        timePickerDialog.show()
    }
}