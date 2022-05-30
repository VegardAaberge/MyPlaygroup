package com.myplaygroup.app.feature_main.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import com.myplaygroup.app.feature_main.presentation.home.components.CalendarScreen

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    viewModel.mainViewModel = mainViewModel

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CalendarScreen(viewModel.calendarFlow)
    }
}