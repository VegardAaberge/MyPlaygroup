package com.myplaygroup.app.feature_main.presentation.admin.classes

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.calendar_classes.CalendarClassesScreen
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.admin.AdminState
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.myplaygroup.app.feature_main.presentation.admin.classes.components.CreateClassesSection
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode

@Composable
fun ClassesScreen(
    adminViewModel: AdminViewModel,
    viewModel: ClassesViewModel = hiltViewModel(),
) {
    adminViewModel.state = adminViewModel.state.copy(
        actionButton = AdminState.ActionButton(
            action = {
                viewModel.onEvent(ClassesScreenEvent.ToggleCreateClassesSection)
            },
            icon = Icons.Default.DateRange
        )
    )

    val scaffoldState = collectEventFlow(viewModel = viewModel)

    val state = viewModel.state;

    val calendarState = rememberSelectableCalendarState(
        initialSelectionMode = SelectionMode.Single,
        confirmSelectionChange = {
            viewModel.onEvent(ClassesScreenEvent.SelectedNewDate(it.firstOrNull()))
            true
        }
    )

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column() {
            AnimatedVisibility(
                visible = state.isCreateVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                CreateClassesSection()
            }

            CalendarClassesScreen(
                selectedDay = state.selectedDate,
                calendarState = calendarState,
                classes = state.dailyClasses
            )
        }
    }
}