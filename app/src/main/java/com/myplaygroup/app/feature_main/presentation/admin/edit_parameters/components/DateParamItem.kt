package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.myplaygroup.app.core.presentation.components.ReadonlyTextField
import com.myplaygroup.app.feature_main.domain.model.ParameterItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateParamItem(
    item: ParameterItem,
    timeChanged: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val itemDate = item.value as LocalDate

    val time = itemDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            timeChanged(LocalDate.of(year, month, dayOfMonth))
        }, itemDate.year, itemDate.monthValue, itemDate.dayOfMonth
    )

    ReadonlyTextField(label = item.key, fieldValue = time) {
        datePickerDialog.show()
    }
}