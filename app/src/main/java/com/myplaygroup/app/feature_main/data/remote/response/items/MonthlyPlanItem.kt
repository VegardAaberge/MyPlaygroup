package com.myplaygroup.app.feature_main.data.remote.response.items

import kotlinx.serialization.Serializable

@Serializable
data class MonthlyPlanItem(
    val id: Long,
    val kidName: String,
    val paid: Boolean,
    val planName: String,
    val planPrice: Int
)