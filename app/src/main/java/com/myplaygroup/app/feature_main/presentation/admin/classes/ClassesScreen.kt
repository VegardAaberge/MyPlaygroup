package com.myplaygroup.app.feature_main.presentation.admin.classes

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.calendar_classes.CalendarClassesScreen
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.presentation.admin.AdminScreenEvent
import com.myplaygroup.app.feature_main.presentation.admin.AdminState
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.myplaygroup.app.feature_main.presentation.admin.classes.components.CreateClassesSection
import com.myplaygroup.app.feature_main.presentation.admin.classes.components.SelectedClassDialog
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode

@Composable
fun ClassesScreen(
    adminViewModel: AdminViewModel,
    viewModel: ClassesViewModel,
) {
    CreateToolbarActionItems(
        viewModel = viewModel,
        adminViewModel = adminViewModel
    )

    CatchOnResume(viewModel)

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
                cardSelected = { dailyClass ->
                    if (dailyClass.id == -1L) {
                        viewModel.onEvent(ClassesScreenEvent.ClassSelected(dailyClass))
                    } else {
                        adminViewModel.onEvent(
                            AdminScreenEvent.NavigateToEditScreen(
                                type = ParametersType.CLASSES,
                                clientId = dailyClass.id.toString()
                            )
                        )
                    }
                }
            )

            state.selectedClass?.let { selectedClass ->
                Dialog(
                    onDismissRequest = {
                        viewModel.onEvent(ClassesScreenEvent.ClassSelected(null))
                    },
                    properties = DialogProperties()
                ) {
                    SelectedClassDialog(
                        selectedClass = selectedClass,
                        submit = { startTime, endTime, classDate, delete ->
                            viewModel.onEvent(ClassesScreenEvent.SubmitSelectedClassTapped(
                                startTime, endTime, classDate, delete
                            ))
                        }
                    )
                }
            }
        }

        if(viewModel.isBusy || adminViewModel.isBusy){
            CustomProgressIndicator()
        }
    }
}

@Composable
private fun CreateToolbarActionItems(
    viewModel: ClassesViewModel,
    adminViewModel: AdminViewModel
){
    val icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_cloud_upload_24)
    LaunchedEffect(key1 = viewModel.getUnsyncedDailyClasses()){
        val actionButtons = mutableListOf<AdminState.ActionButton>()
        if(viewModel.getUnsyncedDailyClasses().any()){
            actionButtons.add(
                AdminState.ActionButton(
                    action = {
                        viewModel.onEvent(ClassesScreenEvent.UploadCreatedClasses)
                    },
                    icon = icon
                ),
            )
        }
        actionButtons.add(
            AdminState.ActionButton(
                action = {
                    viewModel.onEvent(ClassesScreenEvent.ToggleCreateClassesSection)
                },
                icon = Icons.Default.DateRange
            )
        )

        adminViewModel.state = adminViewModel.state.copy(
            actionButtons = actionButtons
        )
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

@Composable
fun CatchOnResume(
    viewModel: ClassesViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_RESUME){
                viewModel.onEvent(ClassesScreenEvent.RefreshData)
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}