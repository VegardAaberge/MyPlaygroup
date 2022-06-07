package com.myplaygroup.app.feature_main.presentation.admin.classes

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.calendar_classes.CalendarClassesScreen
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.admin.AdminState
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.myplaygroup.app.feature_main.presentation.admin.classes.components.CreateClassesSection
import com.myplaygroup.app.feature_main.presentation.admin.classes.components.SelectedClassDialog
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode

@Composable
fun ClassesScreen(
    adminViewModel: AdminViewModel,
    viewModel: ClassesViewModel = hiltViewModel(),
) {
    adminViewModel.state = adminViewModel.state.copy(
        actionButtons = listOf(
            AdminState.ActionButton(
                action = {
                    viewModel.onEvent(ClassesScreenEvent.UploadCreatedClasses)
                },
                icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_cloud_upload_24)
            ),
            AdminState.ActionButton(
                action = {
                    viewModel.onEvent(ClassesScreenEvent.ToggleCreateClassesSection)
                },
                icon = Icons.Default.DateRange
            )
        )
    )

    val scaffoldState = collectEventFlow(viewModel = viewModel)

    val calendarState = rememberSelectableCalendarState(
        initialSelectionMode = SelectionMode.Single,
        confirmSelectionChange = {
            viewModel.onEvent(ClassesScreenEvent.SelectedNewDate(it.firstOrNull()))
            true
        }
    )
    LaunchedEffect(key1 = calendarState){
        viewModel.state = viewModel.state.copy(
            calendarState = calendarState,
        )
    }
    val state = viewModel.state

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            CreateClassesAnimatedSection(
                state = state,
                viewModel = viewModel
            )

            CalendarClassesScreen(
                selectedDay = state.selectedDate,
                calendarState = calendarState,
                classes = state.dailyClasses,
                cardSelected = {
                    viewModel.onEvent(ClassesScreenEvent.ClassSelected(it))
                }
            )

            if(state.selectedClass != null) {
                SelectedClassDialog(
                    selectedClass = state.selectedClass,
                    submit = { startTime, endTime, classDate ->
                        viewModel.onEvent(
                            ClassesScreenEvent.SubmitSelectedClassTapped(startTime, endTime, classDate)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun CreateClassesAnimatedSection(
    state: ClassesState,
    viewModel: ClassesViewModel
) {

    AnimatedVisibility(
        visible = state.isCreateVisible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        CreateClassesSection(
            classType = state.dailyClassType,
            weekdays = state.weekdays,
            startTime = state.startTime,
            endTime = state.endTime,
            classChanged = {
                viewModel.onEvent(ClassesScreenEvent.ClassChanged(it))
            },
            weekdayChanged = {
                viewModel.onEvent(ClassesScreenEvent.WeekdayChanged(it))
            },
            startTimeChanged = {
                viewModel.onEvent(ClassesScreenEvent.StartTimeChanged(it))
            },
            endTimeChanged = {
                viewModel.onEvent(ClassesScreenEvent.EndTimeChanged(it))
            },
            generate = {
                viewModel.onEvent(ClassesScreenEvent.GenerateClassesTapped)
            }
        )
    }
}