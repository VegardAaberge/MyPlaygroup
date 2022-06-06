package com.myplaygroup.app.feature_main.domain.model

import java.time.LocalDate
import java.time.LocalTime


data class DailyClass(
    val id: Long = -1L,
    val classType: String,
    val date: java.time.LocalDate,
    val endTime: LocalTime,
    val startTime: LocalTime,
    val cancelled: Boolean = false,
)