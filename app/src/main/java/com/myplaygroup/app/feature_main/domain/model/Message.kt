package com.myplaygroup.app.feature_main.domain.model

import java.util.*

data class Message (
    val id: Long = -1L,
    val clientId: String = UUID.randomUUID().toString(),
    val message: String,
    val profileName: String,
    val createdBy: String,
    val created: Long,
    var isSending: Boolean = false
){
    fun isSynced() : Boolean {
        return id >= 0
    }
}