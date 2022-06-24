package com.myplaygroup.app.feature_main.presentation.admin.chat_groups

sealed class ChatGroupsScreenEvent {
    object DisconnectFromChat : ChatGroupsScreenEvent()
    data class ConnectToChat(val username: String) : ChatGroupsScreenEvent()
}
