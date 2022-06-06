package com.myplaygroup.app.feature_main.presentation.admin.classes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.model.DailyClassType
import java.time.DayOfWeek

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
                .padding(top = 5.dp)
                .background(Color.White, RoundedCornerShape(10.dp))
                .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
        ){
            Text(text = selectedClass.classType)

            Text(text = selectedClass.dayOfWeek.name)

            LabeledClassTime(
                title = "Start Time",
                classTime = selectedClass.startTime,
                timeChanged = {

                },
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            LabeledClassTime(
                title = "End Time",
                classTime = selectedClass.endTime,
                timeChanged = {

                },
                modifier = Modifier.padding(start = 16.dp),
            )

            Spacer(modifier = Modifier.height(10.dp))

            LabeledClassDate(
                title = "Class Date",
                classDate = selectedClass.date,
                timeChanged = {

                },
                modifier = Modifier.padding(start = 16.dp),
            )

            Button(onClick = {  }) {
                Text(text = "Submit")
            }
        }
    }
}