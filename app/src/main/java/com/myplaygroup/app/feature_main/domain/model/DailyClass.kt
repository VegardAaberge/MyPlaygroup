package com.myplaygroup.app.feature_main.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime


data class DailyClass(
    val id: Long = -1L,
    val classType: String,
    val date: LocalDate,
    val endTime: LocalTime,
    val startTime: LocalTime,
    val dayOfWeek: DayOfWeek,
    val kids : List<String> = listOf(),
    val cancelled: Boolean = false,
    val modified: Boolean = id == -1L
)