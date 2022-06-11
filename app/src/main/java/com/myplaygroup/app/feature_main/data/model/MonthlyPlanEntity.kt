package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity
data class MonthlyPlanEntity(
    @PrimaryKey val id: Long,
    val username: String,
    val kidName: String,
    val startDate: String,
    val endDate: String,
    val planName: String,
    val daysOfWeek: List<String>,
    val planPrice: Long,

    @Transient
    val modified: Boolean = id == -1L
)