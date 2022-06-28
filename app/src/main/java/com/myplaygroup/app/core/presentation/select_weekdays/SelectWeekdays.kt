package com.myplaygroup.app.core.presentation.select_weekdays

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.feature_main.presentation.admin.classes.components.LabelledCheckbox
import java.time.DayOfWeek
import java.time.DayOfWeek.*

@Composable
fun ColumnScope.SelectWeekdays(
    weekdays: Map<DayOfWeek, Boolean>,
    weekdaysError: String? = null,
    enabled: Boolean = true,
    weekdayChanged: (DayOfWeek) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 3.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Column {
            LabelledCheckbox(MONDAY, weekdays[MONDAY] ?: false, weekdayChanged, enabled)
            LabelledCheckbox(THURSDAY, weekdays[THURSDAY] ?: false, weekdayChanged, enabled)
        }
        Column {
            LabelledCheckbox(TUESDAY, weekdays[TUESDAY] ?: false, weekdayChanged, enabled)
            LabelledCheckbox(FRIDAY, weekdays[FRIDAY] ?: false, weekdayChanged, enabled)
        }
        Column {
            LabelledCheckbox(WEDNESDAY, weekdays[WEDNESDAY] ?: false, weekdayChanged, enabled)
            LabelledCheckbox(SATURDAY, weekdays[SATURDAY] ?: false, weekdayChanged, enabled)
        }
    }

    if(weekdaysError != null){
        Text(
            text = weekdaysError,
            color = MaterialTheme.colors.error,
            modifier = Modifier.align(Alignment.Start)
        )
    }
}