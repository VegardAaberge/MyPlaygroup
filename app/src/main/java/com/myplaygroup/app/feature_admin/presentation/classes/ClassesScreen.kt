package com.myplaygroup.app.feature_admin.presentation.classes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_admin.domain.model.DailyClass
import com.myplaygroup.app.feature_admin.presentation.classes.components.CalendarClassesScreen
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun ClassesScreen(
    classesViewModel: ClassesViewModel = hiltViewModel()
) {
    val scaffoldState = collectEventFlow(viewModel = classesViewModel)

    val dailyClasses = classesViewModel.state.dailyClasses;

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
    ) {
        CalendarClassesScreen(classes = dailyClasses)
    }
}