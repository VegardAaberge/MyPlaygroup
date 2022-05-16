package com.myplaygroup.app.feature_main.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.myplaygroup.app.feature_main.data.local.Converters
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
data class MessageEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val message: String,
    val profileName: String?,
    val created: String,
    val createdBy: String
)