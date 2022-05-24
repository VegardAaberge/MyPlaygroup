package com.myplaygroup.app.feature_main.presentation.chat

sealed class ChatScreenEvent {
    data class EnteredNewMessage(val newMessage: String) : ChatScreenEvent()
    object ConnectToChat : ChatScreenEvent()
    object DisconnectFromChat : ChatScreenEvent()
    object SendMessageTapped : ChatScreenEvent()
}
