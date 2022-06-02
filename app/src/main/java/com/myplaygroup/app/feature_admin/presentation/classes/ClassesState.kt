package com.myplaygroup.app.feature_admin.presentation.classes

import com.myplaygroup.app.feature_admin.domain.model.DailyClass

data class ClassesState (
    val dailyClasses: List<DailyClass> = emptyList(),
    val isLoading: Boolean = false
)