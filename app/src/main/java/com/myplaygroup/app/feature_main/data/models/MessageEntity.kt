package com.myplaygroup.app.feature_main.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MessageEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val created: Long,
    val message: String,
    val owner: String,
    val receivers: List<String>
)