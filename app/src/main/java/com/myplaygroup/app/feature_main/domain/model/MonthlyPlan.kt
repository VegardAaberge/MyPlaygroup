package com.myplaygroup.app.feature_main.domain.model

import java.util.*

data class MonthlyPlan(
    val id: Long = -1,
    val clientId: String = UUID.randomUUID().toString(),
    val kidName: String,
    val paid: Boolean,
    val planName: String,
    val planPrice: Int
)
