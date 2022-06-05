package com.myplaygroup.app.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.*

@Entity
data class DailyClassEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val serverId: Long = -1,
    val cancelled: Boolean,
    val classType: String,
    val date: String,
    val endTime: String,
    val startTime: String
)