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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import com.myplaygroup.app.feature_main.presentation.chat.components.MessageBox
import com.myplaygroup.app.feature_main.presentation.chat.components.MessageItem
import com.ramcosta.composedestinations.annotation.Destination

@Destination
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
    val profileImage = viewModel.state.profileImage

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
                items(messages){ message ->
                    MessageItem(
                        isOwner = username == message.createdBy,
                        message = message,
                        iconUri = profileImage,
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

                Spacer(modifier = Modifier.height(65.dp))
            }
        }

        if(viewModel.state.showProgressIndicator){
            CustomProgressIndicator(0f)
        }
    }
}