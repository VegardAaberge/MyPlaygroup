package com.myplaygroup.app.feature_main.presentation.admin.classes

import com.myplaygroup.app.feature_main.domain.model.DailyClass
import java.time.LocalDate

data class ClassesState (
    val isCreateVisible: Boolean = false,
    val dailyClasses: List<DailyClass> = emptyList(),
    val selectedDate: LocalDate? = null,
    val datesToCreate: List<LocalDate> = emptyList(),
    val isLoading: Boolean = false
)