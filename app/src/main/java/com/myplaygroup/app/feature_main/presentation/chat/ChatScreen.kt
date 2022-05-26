package com.myplaygroup.app.feature_main.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun ChatScreen(
    mainViewModel: MainViewModel,
    viewModel: ChatViewModel = hiltViewModel()
) {
    viewModel.mainViewModel = mainViewModel

    val messages = viewModel.state.messages
    val newMessage = viewModel.state.newMessage
    val username = mainViewModel.username.collectAsState(
        initial = Constants.NO_VALUE
    ).value

    val showProgressIndicator = viewModel.state.showProgressIndicator

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner){
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_START) {
                viewModel.onEvent(ChatScreenEvent.ConnectToChat)
            }else if(event == Lifecycle.Event.ON_STOP){
                viewModel.onEvent(ChatScreenEvent.DisconnectFromChat)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                reverseLayout = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){
                items(messages){ message ->
                    MessageItem(
                        isOwner = username == message.createdBy,
                        message = message
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                TextField(
                    value = newMessage,
                    placeholder = {
                        Text(text = "Enter Message")
                    },
                    onValueChange = {
                        viewModel.onEvent(ChatScreenEvent.EnteredNewMessage(it))
                    },
                    modifier = Modifier
                        .weight(1f)
                )

                IconButton(
                    modifier = Modifier
                        .width(50.dp),
                    onClick = {
                        viewModel.onEvent(ChatScreenEvent.SendMessageTapped)
                    })
                {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send"
                    )
                }
            }
        }

        if(viewModel.state.showProgressIndicator){
            CustomProgressIndicator(0f)
        }
    }
}

@Composable
fun MessageItem(
    isOwner : Boolean,
    message: Message
) {
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
                start = if (isOwner) 10.dp else 50.dp,
                end = if (isOwner) 50.dp else 10.dp,
            )
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = if (isOwner) Color.Blue else Color.LightGray,
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
                    start = if (isOwner) 10.dp else 50.dp,
                    end = if (isOwner) 50.dp else 10.dp,
                )) {

            Text(text = message.profileName)
            Text(text = LocalDateTime
                .ofEpochSecond(message.created, 0, ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
                .toString()
            )
        }

        Divider(modifier = Modifier.padding(
            horizontal = 16.dp
        ))
    }
}