package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.*

@Entity
data class MonthlyPlanEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val serverId: Long = -1,
    val kidName: String,
    val paid: Boolean,
    val planName: String,
    val planPrice: Int
)