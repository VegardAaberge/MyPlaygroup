package com.myplaygroup.app.feature_main.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageEntity(
    @PrimaryKey val id: String,
    val serverId: Long = -1,
    val message: String,
    val profileName: String,
    val created: String,
    val createdBy: String,
)