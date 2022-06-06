package com.myplaygroup.app.feature_main.presentation.admin.classes

import com.myplaygroup.app.feature_main.domain.model.DailyClassType
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

data class ClassesState (
    val calendarState: CalendarState<DynamicSelectionState>? = null,

    val isCreateVisible: Boolean = false,
    val dailyClasses: List<DailyClass> = emptyList(),
    val selectedDate: LocalDate? = null,
    val selectedClass: DailyClass? = null,

    val dailyClassType: DailyClassType = DailyClassType.MORNING,
    val startTime: LocalTime = LocalTime.of(9, 30),
    val endTime: LocalTime = LocalTime.of(11, 30),
    val weekdays: EnumMap<DayOfWeek, Boolean> = EnumMap(DayOfWeek::class.java),

    val isLoading: Boolean = false
)