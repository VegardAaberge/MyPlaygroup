package com.myplaygroup.app.feature_main.presentation.admin.chat_groups

import com.myplaygroup.app.feature_main.domain.model.ChatGroup

sealed class ChatGroupsScreenEvent {
    object DisconnectFromChat : ChatGroupsScreenEvent()
    data class ConnectToChat(val username: String) : ChatGroupsScreenEvent()
    data class ResetNotifications(val chatGroup: ChatGroup) : ChatGroupsScreenEvent()
}
