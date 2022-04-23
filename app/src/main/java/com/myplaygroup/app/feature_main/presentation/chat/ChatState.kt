package com.myplaygroup.app.feature_main.presentation.chat

import com.myplaygroup.app.feature_main.domain.model.Message

data class ChatState (
    val isBusy: Boolean = false,
    val messages: List<Message> = emptyList()
)
