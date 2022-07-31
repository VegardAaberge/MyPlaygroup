package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan

data class MonthlyPlansState (
    val monthlyPlans: Map<String, List<MonthlyPlan>> = emptyMap(),
    val showCreateMonthlyPlan: Boolean = false,
    val isSearching: Boolean = false,
    val searchValue: String = ""
)