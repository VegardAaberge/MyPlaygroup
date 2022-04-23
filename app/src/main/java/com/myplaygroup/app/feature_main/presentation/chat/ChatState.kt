package com.myplaygroup.app.feature_main.presentation.chat

import com.myplaygroup.app.feature_main.domain.model.Message

data class ChatState (
    val isLoading: Boolean = false,
    val showProgressIndicator: Boolean = false,
    val messages: List<Message> = emptyList()
)
