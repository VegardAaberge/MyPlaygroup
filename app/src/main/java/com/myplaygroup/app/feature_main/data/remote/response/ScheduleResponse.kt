package com.myplaygroup.app.feature_main.data.remote.response

import com.myplaygroup.app.feature_main.data.model.DailyClassEntity
import com.myplaygroup.app.feature_main.data.model.MonthlyPlanEntity
import com.myplaygroup.app.feature_main.data.model.PaymentEntity
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    val dailyClasses: List<DailyClassEntity>,
    val monthlyPlans: List<MonthlyPlanEntity>,
    val payments: List<PaymentEntity>,
    val userCredit: Int,
    val username: String
)