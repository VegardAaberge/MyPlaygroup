package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@Entity
data class MonthlyPlanEntity(
    @PrimaryKey val id: Long,
    val kidName: String,
    val paid: Boolean,
    val planName: String,
    val planPrice: Int
)