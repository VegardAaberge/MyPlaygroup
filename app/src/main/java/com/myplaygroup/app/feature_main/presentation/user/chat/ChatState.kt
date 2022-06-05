package com.myplaygroup.app.feature_main.presentation.user.chat

import android.net.Uri
import com.myplaygroup.app.feature_main.domain.model.Message

data class ChatState (
    val isLoading: Boolean = false,
    val showProgressIndicator: Boolean = false,
    val newMessage: String = "",
    val profileImage: Uri? = null,
    val messages: List<Message> = emptyList()
)
