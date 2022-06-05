package com.myplaygroup.app.feature_admin.presentation.classes

import com.myplaygroup.app.feature_admin.domain.model.DailyClass
import java.time.LocalDate

data class ClassesState (
    val dailyClasses: List<DailyClass> = emptyList(),
    val selectedDate: LocalDate? = null,
    val datesToCreate: List<LocalDate> = emptyList(),
    val isLoading: Boolean = false
)