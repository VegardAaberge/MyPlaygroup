package com.myplaygroup.app.feature_main.presentation.admin.classes.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.myplaygroup.app.core.presentation.components.ReadonlyTextField
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun LabeledClassDate(
    title: String,
    classDate: LocalDate,
    modifier: Modifier = Modifier,
    timeChanged: (LocalDate) -> Unit
){
    val context = LocalContext.current
    val time = classDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            timeChanged(LocalDate.of(year, month, dayOfMonth))
        }, classDate.year, classDate.monthValue, classDate.dayOfMonth
    )

    ReadonlyTextField(label = title, fieldValue = time, modifier = modifier) {
        datePickerDialog.show()
    }
}