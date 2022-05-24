package com.myplaygroup.app.feature_main.domain.model

import java.time.LocalDateTime

data class Message (
    val serverId: Long = 1,
    val message: String,
    val profileName: String,
    val createdBy: String,
    val created: Long
){
    fun isSynced() : Boolean {
        return serverId == -1L
    }
}