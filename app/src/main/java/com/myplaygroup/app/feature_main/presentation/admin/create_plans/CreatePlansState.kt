package com.myplaygroup.app.feature_main.presentation.admin.create_plans

import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan

data class CreatePlansState(
    val users: List<AppUser> = emptyList(),
    val monthlyPlans: List<MonthlyPlan> = emptyList(),
    val isLoading: Boolean = false
)
