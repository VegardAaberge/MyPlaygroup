package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.select_weekdays.SelectWeekdays
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType
import com.myplaygroup.app.feature_main.domain.model.ParameterItem
import java.time.DayOfWeek
import java.time.DayOfWeek.*
import java.util.*

@Suppress("UNCHECKED_CAST")
@Composable
fun SelectWeekdaysItem(
    item: ParameterItem,
    selectWeekdayChanged: (Any, String) -> Unit
) {
    val weekdaysList = (item.value as List<DayOfWeek>).toMutableList()
    val weekdays : EnumMap<DayOfWeek, Boolean> = EnumMap(DayOfWeek::class.java)
    weekdays.put(MONDAY, weekdaysList.contains(MONDAY))
    weekdays.put(TUESDAY, weekdaysList.contains(TUESDAY))
    weekdays.put(WEDNESDAY, weekdaysList.contains(WEDNESDAY))
    weekdays.put(THURSDAY, weekdaysList.contains(THURSDAY))
    weekdays.put(FRIDAY, weekdaysList.contains(FRIDAY))
    weekdays.put(SATURDAY, weekdaysList.contains(SATURDAY))

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = item.getTitle(),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )

        SelectWeekdays(
            weekdays = weekdays,
            weekdaysError = item.error,
            weekdayChanged = { dayOfWeek ->
                focusManager.clearFocus()
                if(item.enabled){
                    if (weekdaysList.contains(dayOfWeek)) {
                        weekdaysList.remove(dayOfWeek)
                    } else {
                        weekdaysList.add(dayOfWeek)
                    }

                    selectWeekdayChanged(weekdaysList, item.key)
                }
            },
            enabled = item.enabled
        )
    }

    Divider(modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
fun SelectWeekdaysItemPreview() {
    MyPlaygroupTheme {
        val weekdays : List<DayOfWeek> = listOf(
            MONDAY,
            TUESDAY
        )

        SelectWeekdaysItem(
            item = ParameterItem(
                ParameterDisplayType.WEEKDAYS,
                key = "Key",
                value = weekdays
            ),
        ){ value, key ->

        }
    }
}