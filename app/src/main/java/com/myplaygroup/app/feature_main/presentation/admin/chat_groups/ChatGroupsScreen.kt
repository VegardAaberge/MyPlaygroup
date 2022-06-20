package com.myplaygroup.app.feature_main.presentation.admin.chat_groups

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.myplaygroup.app.feature_main.presentation.admin.chat_groups.components.ChatGroupItem

@Composable
fun ChatGroupsScreen(
    adminViewModel: AdminViewModel,
    viewModel: ChatGroupsViewModel = hiltViewModel(),
) {
    val scaffoldState = collectEventFlow(viewModel = viewModel)

    LaunchedEffect(key1 = adminViewModel.state.title){
        adminViewModel.state = adminViewModel.state.copy(
            actionButtons = listOf(),
        )
    }

    val state = viewModel.state

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.padding(8.dp)
        ){
            items(state.chatGroups) { item ->
                ChatGroupItem(item)

                Divider(modifier = Modifier.fillMaxWidth())
            }
        }

        if(viewModel.isBusy || adminViewModel.isBusy){
            CustomProgressIndicator()
        }
    }
}