package com.myplaygroup.app.feature_main.domain.model

import android.net.Uri
import java.time.LocalDateTime

data class ChatGroup(
    val username: String,
    val lastMessage: String?,
    val updateTime: LocalDateTime?,
    val notification: Int = 0,
    val icon: Uri? = null,
)
