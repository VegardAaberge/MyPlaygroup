package com.myplaygroup.app.feature_main.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    val username = viewModel.mainViewModel.state.username

    val showProgressIndicator = viewModel.state.showProgressIndicator

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(messages){ message ->

                val isOwner = username == message.createdBy

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 10.dp,
                            bottom = 10.dp,
                            start = if(isOwner) 10.dp else 50.dp,
                            end = if(isOwner) 50.dp else 10.dp,
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            color = if(isOwner) Color.Blue else Color.LightGray,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(10.dp)
                    ){
                        Text(
                            color = if(isOwner) Color.White else Color.Unspecified,
                            text = message.message
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = if(isOwner) 10.dp else 50.dp,
                            end = if(isOwner) 50.dp else 10.dp,
                        )) {

                        Text(text = "${message.createdBy}")
                        Text(text = "${message.created}")
                    }

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