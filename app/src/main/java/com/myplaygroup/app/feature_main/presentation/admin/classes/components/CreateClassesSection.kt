package com.myplaygroup.app.feature_main.presentation.admin.classes.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.feature_main.domain.enums.DailyClassType
import com.myplaygroup.app.feature_main.domain.enums.DayOfWeek
import com.myplaygroup.app.feature_main.domain.enums.DayOfWeek.*
import java.time.LocalTime
import java.util.*

@Composable
fun CreateClassesSection(
    classType: DailyClassType,
    startTime: LocalTime,
    endTime: LocalTime,
    weekdays: EnumMap<DayOfWeek, Boolean>,
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
            options = listOf(DailyClassType.MORNING.name, DailyClassType.EVENING.name),
            classType = classType,
            classChanged = classChanged,
            modifier = Modifier.padding(start = 14.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        LabeledClassTime(
            title = "Start Time",
            classTime = startTime,
            timeChanged = startTimeChanged,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LabeledClassTime(
            title = "End Time",
            classTime = endTime,
            timeChanged = endTimeChanged,
            modifier = Modifier.padding(start = 16.dp),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 3.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            Column {
                LabelledCheckbox(MON.name, weekdays[MON] ?: false, weekdayChanged)
                LabelledCheckbox(THU.name, weekdays[THU] ?: false, weekdayChanged)
            }
            Column {
                LabelledCheckbox(TUE.name, weekdays[TUE] ?: false, weekdayChanged)
                LabelledCheckbox(FRI.name, weekdays[FRI] ?: false, weekdayChanged)
            }
            Column {
                LabelledCheckbox(WED.name, weekdays[WED] ?: false, weekdayChanged)
                LabelledCheckbox(SAT.name, weekdays[SAT] ?: false, weekdayChanged)
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

@Composable
fun LabelledCheckbox(
    title: String,
    isChecked: Boolean,
    weekdayChanged: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                weekdayChanged(DayOfWeek.valueOf(title))
            },
            enabled = true,
            colors = CheckboxDefaults.colors()
        )
        Text(text = title)
    }
}

@Composable
fun LabeledRadioGroup(
    options: List<String>,
    modifier: Modifier = Modifier,
    classType: DailyClassType,
    classChanged: (DailyClassType) -> Unit
){
    Row(modifier = modifier) {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f, false)
                    .clickable {
                        classChanged(DailyClassType.valueOf(option))
                    },
            ) {
                RadioButton(selected = classType.name == option, onClick = null)
                Text(text = option)
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

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
            .width(130.dp)
            .clickable {
                timePickerDialog.show()
            },
    ) {
        Text(text = "$title:")
        Text(text = time)
    }
}
