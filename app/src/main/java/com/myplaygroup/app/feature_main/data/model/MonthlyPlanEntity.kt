package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
@Entity
data class MonthlyPlanEntity(
    @PrimaryKey val clientId: String = UUID.randomUUID().toString(),
    val id: Long = -1,
    val username: String,
    val kidName: String,
    val startDate: String,
    val endDate: String,
    val planName: String,
    val daysOfWeek: List<String>,
    val planPrice: Long,
    val availableClasses: Int,
    val cancelledClasses: Int,
    val cancelled: Boolean,
    val changeDays: Boolean = false,

    @Transient
    val modified: Boolean = id == -1L
)