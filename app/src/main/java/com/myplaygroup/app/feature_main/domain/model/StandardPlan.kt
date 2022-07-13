package com.myplaygroup.app.feature_main.domain.model

import com.myplaygroup.app.feature_main.domain.enums.DailyClassType

data class StandardPlan(
    val name: String,
    val price: Int,
    val type: DailyClassType
)
