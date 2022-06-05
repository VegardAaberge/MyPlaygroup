package com.myplaygroup.app.feature_main.data.remote.response

import com.myplaygroup.app.feature_main.data.remote.response.items.DailyClassItem
import com.myplaygroup.app.feature_main.data.remote.response.items.MonthlyPlanItem
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyPlansResponse(
    val dailyClasses: List<DailyClassItem>,
    val monthlyPlans: List<MonthlyPlanItem>,
    val userCredit: Int,
    val username: String
)