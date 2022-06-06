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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalTime

@Composable
fun LabeledClassTime(
    title: String,
    classTime: LocalTime,
    modifier: Modifier = Modifier,
    timeChanged: (LocalTime) -> Unit
){
    val context = LocalContext.current
    val time = "${classTime.hour}:${classTime.minute}"

    val timePickerDialog = TimePickerDialog(
        context,
        {_, hour : Int, minute: Int ->
            timeChanged(LocalTime.of(hour, minute))
        }, classTime.hour, classTime.minute, false
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .width(200.dp)
            .clickable {
                timePickerDialog.show()
            },

    ) {
        Text(text = "$title:")
        Text(text = time)
    }
}