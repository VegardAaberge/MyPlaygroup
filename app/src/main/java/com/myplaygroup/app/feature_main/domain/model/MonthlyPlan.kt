package com.myplaygroup.app.feature_main.domain.model

import java.time.DayOfWeek
import java.time.LocalDate

data class MonthlyPlan(
    val id: Long = -1,
    val username: String,
    val kidName: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val planName: String,
    val daysOfWeek: List<DayOfWeek>,
    val planPrice: Long,
    val modified: Boolean = id == -1L
)
