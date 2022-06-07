package com.myplaygroup.app.feature_main.presentation.admin.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel

@Composable
fun UsersScreen(
    adminViewModel: AdminViewModel,
    usersViewModel: UsersViewModel = hiltViewModel()
) {
    adminViewModel.state = adminViewModel.state.copy(
        actionButtons = listOf()
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Users Screen")
    }
}