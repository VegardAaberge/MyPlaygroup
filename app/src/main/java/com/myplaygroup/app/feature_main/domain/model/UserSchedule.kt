package com.myplaygroup.app.feature_main.domain.model

data class UserSchedule (
    val dailyClasses: List<DailyClass>,
    val monthlyPlans: List<MonthlyPlan>,
    val payments: List<Payment>,
)