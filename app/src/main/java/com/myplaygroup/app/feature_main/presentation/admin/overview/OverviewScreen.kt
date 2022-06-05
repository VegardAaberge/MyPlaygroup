package com.myplaygroup.app.feature_main.presentation.admin.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.feature_main.presentation.admin.AdminState
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel

@Composable
fun OverviewScreen(
    adminViewModel: AdminViewModel,
    overviewViewModel: OverviewViewModel = hiltViewModel()
) {
    adminViewModel.state = adminViewModel.state.copy(
        actionButton = null
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Overview Screen")
    }
}