package com.myplaygroup.app.feature_main.presentation.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.calendar_classes.CalendarClassesScreen
import com.myplaygroup.app.feature_admin.presentation.classes.ClassesScreenEvent
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    viewModel.mainViewModel = mainViewModel
    val state = viewModel.state

    val calendarState = rememberSelectableCalendarState(
        initialSelectionMode = SelectionMode.Single,
        confirmSelectionChange = {
            viewModel.onEvent(ClassesScreenEvent.SelectedNewDate(it.firstOrNull()))
            true
        }
    )

    CalendarClassesScreen(
        selectedDay = state.selectedDate,
        calendarState = calendarState,
        classes = state.dailyClasses
    )
}