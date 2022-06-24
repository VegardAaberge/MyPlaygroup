package com.myplaygroup.app.feature_main.presentation.chat

import android.net.Uri
import com.myplaygroup.app.feature_main.domain.model.Message

data class ChatState (
    val isLoading: Boolean = false,
    val showProgressIndicator: Boolean = false,
    val newMessage: String = "",
    val profileImage: Uri? = null,
    val userUri: Map<String, Uri> = emptyMap(),
    val messages: List<Message> = emptyList(),
    val lastReadMessage: List<LastRead> = emptyList(),
){
    data class LastRead(
        val messageClientId : String,
        val profileName: String
    )
}