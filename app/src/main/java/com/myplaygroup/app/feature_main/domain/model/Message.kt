package com.myplaygroup.app.feature_main.domain.model

import java.time.LocalDateTime

data class Message (
    val message: String,
    val profileName: String,
    val createdBy: String,
    val created: Long
)