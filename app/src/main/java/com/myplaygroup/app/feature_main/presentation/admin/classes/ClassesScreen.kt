package com.myplaygroup.app.feature_main.presentation.admin.classes

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.calendar_classes.CalendarClassesScreen
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.admin.AdminState
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode

@Composable
fun ClassesScreen(
    adminViewModel: AdminViewModel,
    classesViewModel: ClassesViewModel = hiltViewModel(),
) {
    adminViewModel.state = adminViewModel.state.copy(
        actionButton = AdminState.ActionButton(
            action = {

            },
            icon = Icons.Default.DateRange
        )
    )

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