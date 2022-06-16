package com.myplaygroup.app.feature_main.presentation.admin.chat_groups

import com.myplaygroup.app.feature_main.domain.model.ChatGroup

data class ChatGroupsState (
    val chatGroups : List<ChatGroup> = emptyList(),
)