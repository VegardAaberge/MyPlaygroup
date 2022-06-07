package com.myplaygroup.app.feature_main.domain.model

import java.time.DayOfWeek
import java.util.*
import kotlin.collections.List

data class MonthlyPlan(
    val id: Long = -1,
    val kidName: String,
    val paid: Boolean,
    val planName: String,
    val daysOfWeek: List<DayOfWeek>,
    val planPrice: Long
)
