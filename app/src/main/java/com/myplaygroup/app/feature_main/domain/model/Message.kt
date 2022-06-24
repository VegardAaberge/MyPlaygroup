package com.myplaygroup.app.feature_main.domain.model

import java.time.LocalDateTime
import java.util.*

data class Message (
    val id: Long = -1L,
    val clientId: String = UUID.randomUUID().toString(),
    val message: String,
    val profileName: String,
    val createdBy: String,
    val receivers: List<String> = emptyList(),
    val created: LocalDateTime,
    var isSending: Boolean = false,
    val readBy: List<String> = emptyList(),
){
    fun isSynced() : Boolean {
        return id >= 0
    }
}