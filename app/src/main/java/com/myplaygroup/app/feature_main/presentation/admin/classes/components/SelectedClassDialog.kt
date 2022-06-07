package com.myplaygroup.app.feature_main.presentation.admin.classes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.BasicTextField
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.model.DailyClassType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun SelectedClassDialog(
    selectedClass: DailyClass,
    submit: (LocalTime, LocalTime, LocalDate, Boolean) -> Unit
) {
    var startTime by remember {
        mutableStateOf(selectedClass.startTime)
    }
    var endTime by remember {
        mutableStateOf(selectedClass.endTime)
    }
    var classDate by remember {
        mutableStateOf(selectedClass.date)
    }
    var cancelled by remember {
        mutableStateOf(selectedClass.cancelled)
    }

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
                width = 170.dp,
                fieldValue = selectedClass.dayOfWeek.name
                    .lowercase()
                    .replaceFirstChar { x -> x.uppercase() },
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .align(Alignment.Start)
            )

            LabeledClassTime(
                title = "Start Time",
                classTime = startTime,
                timeChanged = {
                    startTime = it
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            LabeledClassTime(
                title = "End Time",
                classTime = endTime,
                timeChanged = {
                    endTime = it
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            LabeledClassDate(
                title = "Class Date",
                classDate = classDate,
                timeChanged = {
                    classDate = it
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if(selectedClass.id == -1L){
                        "Delete"
                    } else "Cancelled"
                )
                Switch(
                    colors =SwitchDefaults.colors(
                        checkedThumbColor = colorResource(id = R.color.keynote_red_2),
                        uncheckedThumbColor = colorResource(id = R.color.keynote_red_2),
                        uncheckedTrackColor = Color.LightGray
                    ),
                    onCheckedChange = {
                        cancelled = !cancelled
                    },
                    checked = cancelled
                )
            }

            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if(cancelled && selectedClass.id == -1L){
                        MaterialTheme.colors.error
                    } else MaterialTheme.colors.primary,
                ),
                onClick = {
                    submit(startTime, endTime, classDate, cancelled)
                }
            ) {
                Text(
                    text = if(cancelled && selectedClass.id == -1L){
                        "Delete"
                    } else "Update"
                )
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
        )){ a, b, c, d ->

        }
    }
}