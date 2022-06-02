package com.myplaygroup.app.feature_admin.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class DailyClassItem (
    val id: Long,
    val cancelled: Boolean,
    val classType: String,
    val date: String,
    val endTime: String,
    val startTime: String
)