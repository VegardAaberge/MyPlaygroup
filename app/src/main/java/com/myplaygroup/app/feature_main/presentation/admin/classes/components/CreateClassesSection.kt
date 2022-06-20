package com.myplaygroup.app.feature_main.presentation.admin.classes.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.feature_main.domain.enums.DailyClassType
import java.time.DayOfWeek
import java.time.DayOfWeek.*
import java.time.LocalTime

@Composable
fun CreateClassesSection(
    classType: DailyClassType,
    startTime: LocalTime,
    endTime: LocalTime,
    weekdays: Map<DayOfWeek, Boolean>,
    classChanged: (DailyClassType) -> Unit,
    startTimeChanged: (LocalTime) -> Unit,
    endTimeChanged: (LocalTime) -> Unit,
    weekdayChanged: (DayOfWeek) -> Unit,
    generate: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .padding(end = 16.dp)
            .fillMaxWidth(),
    ) {
        LabeledRadioGroup(
            options = listOf(
                DailyClassType.MORNING.name,
                DailyClassType.EVENING.name,
                DailyClassType.WEEKEND.name
            ),
            classType = classType,
            classChanged = classChanged,
            modifier = Modifier.padding(start = 14.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        LabeledClassTime(
            title = "Start Time",
            classTime = startTime,
            timeChanged = startTimeChanged,
            modifier = Modifier.padding(start = 16.dp),
            basicWidth = 130.dp
        )

        Spacer(modifier = Modifier.height(10.dp))

        LabeledClassTime(
            title = "End Time",
            classTime = endTime,
            timeChanged = endTimeChanged,
            modifier = Modifier.padding(start = 16.dp),
            basicWidth = 130.dp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 3.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            Column {
                LabelledCheckbox(MONDAY, weekdays[MONDAY] ?: false, weekdayChanged)
                LabelledCheckbox(THURSDAY, weekdays[THURSDAY] ?: false, weekdayChanged)
            }
            Column {
                LabelledCheckbox(TUESDAY, weekdays[TUESDAY] ?: false, weekdayChanged)
                LabelledCheckbox(FRIDAY, weekdays[FRIDAY] ?: false, weekdayChanged)
            }
            Column {
                LabelledCheckbox(WEDNESDAY, weekdays[WEDNESDAY] ?: false, weekdayChanged)
                LabelledCheckbox(SATURDAY, weekdays[SATURDAY] ?: false, weekdayChanged)
            }
        }

        Button(
            onClick = {
                generate()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Generate")
        }
    }
}






