package com.myplaygroup.app.feature_main.presentation.admin.classes.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.components.BasicTextField
import com.myplaygroup.app.core.presentation.components.ReadonlyTextField
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun LabeledClassTime(
    title: String,
    classTime: LocalTime,
    modifier: Modifier = Modifier,
    timeChanged: (LocalTime) -> Unit,
    basicWidth: Dp? = null,
){
    val context = LocalContext.current
    val time = classTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    val timePickerDialog = TimePickerDialog(
        context,
        {_, hour : Int, minute: Int ->
            timeChanged(LocalTime.of(hour, minute))
        }, classTime.hour, classTime.minute, false
    )

    if(basicWidth != null){
        BasicTextField(label = title, fieldValue = time, modifier = modifier, width = basicWidth) {
            timePickerDialog.show()
        }
    }else{
        ReadonlyTextField(label = title, fieldValue = time, modifier = modifier) {
            timePickerDialog.show()
        }
    }
}