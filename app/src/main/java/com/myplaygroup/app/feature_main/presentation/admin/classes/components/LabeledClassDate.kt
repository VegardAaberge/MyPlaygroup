package com.myplaygroup.app.feature_main.presentation.admin.classes.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun LabeledClassDate(
    title: String,
    classDate: LocalDate,
    modifier: Modifier = Modifier,
    timeChanged: (LocalTime) -> Unit
){
    val context = LocalContext.current
    val time = "${classDate.year}-${classDate.month}-${classDate.dayOfMonth}"

    val mDate = remember { mutableStateOf("") }

    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth+1}/$mYear"
        }, 2022, 6, 1
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .width(200.dp)
            .clickable {
                mDatePickerDialog.show()
            },
    ) {
        Text(text = "$title:")
        Text(text = time)
    }
}