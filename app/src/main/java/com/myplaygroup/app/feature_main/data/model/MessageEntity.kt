package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class MessageEntity(
    @PrimaryKey val clientId: String,
    val id: Long,
    val message: String,
    val profileName: String?,
    val created: String,
    val createdBy: String,
    val receivers: List<String>,
)