package com.myplaygroup.app.feature_main.domain.model

import java.time.LocalDateTime
import java.util.*

data class Message (
    val id: String = UUID.randomUUID().toString(),
    val serverId: Long = -1L,
    val message: String,
    val profileName: String,
    val createdBy: String,
    val created: Long,
    val hasError: Boolean = false
){
    fun isSynced() : Boolean {
        return serverId >= 0
    }
}