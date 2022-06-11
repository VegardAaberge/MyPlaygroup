package com.myplaygroup.app.feature_main.presentation.admin.create_plans

import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.model.StandardPlan

data class CreatePlansState(
    val users: List<AppUser> = emptyList(),
    val standardPlans: List<StandardPlan> = emptyList(),
    val monthlyPlans: List<MonthlyPlan> = emptyList(),
    val isLoading: Boolean = false
)
