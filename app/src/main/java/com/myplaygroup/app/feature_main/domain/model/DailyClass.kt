package com.myplaygroup.app.feature_main.domain.model


data class DailyClass(
    val id: Long = -1L,
    val cancelled: Boolean,
    val classType: String,
    val date: java.time.LocalDate,
    val endTime: String,
    val startTime: String
)