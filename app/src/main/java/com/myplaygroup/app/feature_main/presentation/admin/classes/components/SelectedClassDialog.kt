package com.myplaygroup.app.feature_main.presentation.admin.classes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myplaygroup.app.core.presentation.components.BasicTextField
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.model.DailyClassType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun SelectedClassDialog(
    selectedClass: DailyClass
) {
    Dialog(
        onDismissRequest = {

        },
        properties = DialogProperties(

        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .width(400.dp)
                .background(Color.White, RoundedCornerShape(10.dp))
                .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
                .padding(16.dp)
        ){

            Text(
                text = selectedClass.classType,
                style = MaterialTheme.typography.h4
            )

            BasicTextField(
                label = "Day of week",
                width = 160.dp,
                fieldValue = selectedClass.dayOfWeek.name,
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .align(Alignment.Start)
            )

            LabeledClassTime(
                title = "Start Time",
                classTime = selectedClass.startTime,
                timeChanged = {

                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            LabeledClassTime(
                title = "End Time",
                classTime = selectedClass.endTime,
                timeChanged = {

                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            LabeledClassDate(
                title = "Class Date",
                classDate = selectedClass.date,
                timeChanged = {

                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                }
            ) {
                Text(text = "Submit")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectedClassDialogPreview() {
    MyPlaygroupTheme {
        SelectedClassDialog(selectedClass = DailyClass(
            id = -1,
            classType = DailyClassType.MORNING.name,
            date = LocalDate.now(),
            endTime = LocalTime.now().plusHours(2),
            startTime = LocalTime.now(),
            dayOfWeek = DayOfWeek.MONDAY
        ))
    }
}