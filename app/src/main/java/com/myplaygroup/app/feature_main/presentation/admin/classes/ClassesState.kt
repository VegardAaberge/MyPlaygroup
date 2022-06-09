package com.myplaygroup.app.feature_main.presentation.admin.classes

import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.enums.DailyClassType
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
    val weekdays: EnumMap<DayOfWeek, Boolean> = initWeekdays(),

    val isLoading: Boolean = false
){
    companion object{
        private fun initWeekdays() : EnumMap<DayOfWeek, Boolean> {
            val weekdays : EnumMap<DayOfWeek, Boolean> = EnumMap(DayOfWeek::class.java)
            weekdays.put(DayOfWeek.MONDAY, true)
            weekdays.put(DayOfWeek.TUESDAY, true)
            weekdays.put(DayOfWeek.WEDNESDAY, true)
            weekdays.put(DayOfWeek.THURSDAY, true)
            weekdays.put(DayOfWeek.FRIDAY, true)
            weekdays.put(DayOfWeek.SATURDAY, false)
            return weekdays
        }
    }
}