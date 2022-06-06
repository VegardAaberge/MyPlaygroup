package com.myplaygroup.app.core.presentation.calendar_classes

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.calendar_classes.components.CalendarCard
import com.myplaygroup.app.core.presentation.calendar_classes.components.CustomDay
import com.myplaygroup.app.core.presentation.calendar_classes.components.MonthHeader
import com.myplaygroup.app.core.presentation.calendar_classes.components.WeekHeader
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun CalendarClassesScreen(
    classes: List<DailyClass>,
    calendarState: CalendarState<DynamicSelectionState>,
    selectedDay: LocalDate?
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SelectableCalendar(
            calendarState = calendarState,
            dayContent = { dayState ->
                CustomDay(
                    state = dayState,
                    dailyClass = classes.firstOrNull { it.date == dayState.date },
                )
            },
            weekHeader = { WeekHeader(daysOfWeek = it) },
            monthHeader = { MonthHeader(monthState = it) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        val selectedClasses = classes.filter { it.date == selectedDay }
        if (selectedClasses.any()) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn {
                    items(selectedClasses) { item ->
                        CalendarCard(dailyClass = item)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CalendarClassesScreenPreview() {
    MyPlaygroupTheme {
        val selectedDay = LocalDate.now().minusDays(1)

        CalendarClassesScreen(
            classes = listOf(
                DailyClass(
                    classType = "Morning Group",
                    date = selectedDay,
                    endTime = LocalTime.of(11, 30),
                    startTime = LocalTime.of(9, 30),
                    cancelled = false,
                    dayOfWeek = DayOfWeek.MONDAY,
                )
            ),
            calendarState = rememberSelectableCalendarState(
                initialSelectionMode = SelectionMode.Single,
            ),
            selectedDay = selectedDay
        )
    }
}