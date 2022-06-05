package com.myplaygroup.app.feature_main.presentation.home

import com.myplaygroup.app.core.domain.model.DailyClass
import java.time.LocalDate

data class HomeState (
    val dailyClasses: List<DailyClass> = emptyList(),
    val selectedDate: LocalDate? = null,
    val isLoading: Boolean = false
)