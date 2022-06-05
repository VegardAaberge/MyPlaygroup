package com.myplaygroup.app.feature_main.domain.model

import com.myplaygroup.app.core.domain.model.DailyClass
import com.myplaygroup.app.core.domain.model.MonthlyPlan

data class UserSchedule (
    val dailyClasses: List<DailyClass>,
    val monthlyPlans: List<MonthlyPlan>,
)