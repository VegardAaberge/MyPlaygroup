package com.myplaygroup.app.feature_main.presentation.chat

import com.myplaygroup.app.feature_main.domain.model.Message

sealed class ChatScreenEvent {
    data class EnteredNewMessage(val newMessage: String) : ChatScreenEvent()
    data class ResendMessage(val message: Message) : ChatScreenEvent()
    data class ConnectToChat(val username: String) : ChatScreenEvent()
    object DisconnectFromChat : ChatScreenEvent()
    object SendMessageTapped : ChatScreenEvent()
}
