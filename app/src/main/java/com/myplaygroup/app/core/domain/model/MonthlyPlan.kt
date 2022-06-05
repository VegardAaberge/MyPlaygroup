package com.myplaygroup.app.core.domain.model

import androidx.room.PrimaryKey
import java.util.*

data class MonthlyPlan(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val serverId: Long = -1,
    val kidName: String,
    val paid: Boolean,
    val planName: String,
    val planPrice: Int
)
