package com.myplaygroup.app.feature_main.presentation.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.feature_main.presentation.MainViewModel

@Composable
fun ChatScreen(
    mainViewModel: MainViewModel,
    viewModel: ChatViewModel = hiltViewModel()
) {
    viewModel.mainViewModel = mainViewModel
    val messages = viewModel.state.messages
    val showProgressIndicator = viewModel.state.showProgressIndicator

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(messages){ message ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "Message: ${message.message}")
                    Text(text = "Owner: ${message.createdBy}")
                    Text(text = "Created: ${message.created}")
                    Divider(modifier = Modifier.padding(
                        horizontal = 16.dp
                    ))
                }
            }
        }

        if(viewModel.state.showProgressIndicator){
            CustomProgressIndicator(0f)
        }
    }
}