package com.myplaygroup.app.feature_admin.presentation.classes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.calendar_classes.CalendarClassesScreen
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode

@Composable
fun ClassesScreen(
    classesViewModel: ClassesViewModel = hiltViewModel()
) {
    val scaffoldState = collectEventFlow(viewModel = classesViewModel)

    val state = classesViewModel.state;

    val calendarState = rememberSelectableCalendarState(
        initialSelectionMode = SelectionMode.Single,
        confirmSelectionChange = {
            classesViewModel.onEvent(ClassesScreenEvent.SelectedNewDate(it.firstOrNull()))
            true
        }
    )

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxWidth(),
    ) {
        CalendarClassesScreen(
            selectedDay = state.selectedDate,
            calendarState = calendarState,
            classes = state.dailyClasses
        )
    }
}