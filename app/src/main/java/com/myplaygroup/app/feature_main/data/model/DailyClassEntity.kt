package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@Entity
data class DailyClassEntity(
    @PrimaryKey val id: Long,
    val cancelled: Boolean,
    val classType: String,
    val date: String,
    val endTime: String,
    val startTime: String
)