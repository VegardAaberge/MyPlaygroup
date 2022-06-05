package com.myplaygroup.app.feature_main.data.remote.response

import com.myplaygroup.app.core.data.remote.responses.DailyClassItem
import com.myplaygroup.app.core.data.remote.responses.MonthlyPlanItem
import kotlinx.serialization.Serializable

@Serializable
data class MonthlyPlansResponse(
    val dailyClasses: List<DailyClassItem>,
    val monthlyPlans: List<MonthlyPlanItem>,
    val userCredit: Int,
    val username: String
)