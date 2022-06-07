package com.myplaygroup.app.feature_main.data.remote.response

import com.myplaygroup.app.feature_main.data.model.DailyClassEntity
import com.myplaygroup.app.feature_main.data.model.MonthlyPlanEntity
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    val dailyClasses: List<DailyClassEntity>,
    val monthlyPlans: List<MonthlyPlanEntity>,
    val userCredit: Int,
    val username: String
)