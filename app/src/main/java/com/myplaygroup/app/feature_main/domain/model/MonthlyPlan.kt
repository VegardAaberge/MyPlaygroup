package com.myplaygroup.app.feature_main.domain.model

import java.time.DayOfWeek
import java.time.Month

data class MonthlyPlan(
    val id: Long = -1,
    val username: String,
    val kidName: String,
    val month: Month,
    val paid: Boolean,
    val planName: String,
    val daysOfWeek: List<DayOfWeek>,
    val planPrice: Long,
    val modified: Boolean = false
)
