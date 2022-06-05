package com.myplaygroup.app.feature_admin.domain.model


data class DailyClass(
    val id: String,
    val cancelled: Boolean,
    val classType: String,
    val date: java.time.LocalDate,
    val endTime: String,
    val startTime: String
)