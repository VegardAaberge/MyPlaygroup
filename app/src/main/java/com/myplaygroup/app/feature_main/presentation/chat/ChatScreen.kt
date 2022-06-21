package com.myplaygroup.app.feature_main.presentation.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.myplaygroup.app.core.presentation.app_bar.AppBarBackButton
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.chat.components.MessageItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ChatScreen(
    receivers: Array<String>,
    isAdmin : Boolean,
    navigator: DestinationsNavigator? = null,
    viewModel: ChatViewModel = hiltViewModel()
) {
    if(viewModel.receivers.isEmpty()){
        viewModel.init(receivers.toList(), isAdmin)
    }

    val scaffoldState = collectEventFlow(viewModel, navigator)

    val username = viewModel.username.collectAsState("").value
    val state = viewModel.state

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner){
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_START) {
                viewModel.onEvent(ChatScreenEvent.ConnectToChat(username))
            }else if(event == Lifecycle.Event.ON_STOP){
                viewModel.onEvent(ChatScreenEvent.DisconnectFromChat)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            if(isAdmin){
                DefaultTopAppBar(
                    title = "Chat: " + viewModel.receivers.joinToString(),
                    navigationIcon = {
                        AppBarBackButton(navigator!!)
                    }
                )
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
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
                items(state.messages){ message ->
                    val isOwner = username == message.createdBy
                    MessageItem(
                        isOwner = isOwner,
                        message = message,
                        iconUri = state.userUri[message.createdBy],
                        resendMessage = {
                            viewModel.onEvent(ChatScreenEvent.ResendMessage(message))
                        }
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, false)
                ) {
                    TextField(
                        value = state.newMessage,
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

                if(!isAdmin){
                    Spacer(modifier = Modifier.height(65.dp))
                }
            }
        }

        if(viewModel.state.showProgressIndicator){
            CustomProgressIndicator(0f)
        }
    }
}