package com.myplaygroup.app.feature_main.presentation.admin.classes

import com.myplaygroup.app.feature_main.domain.model.DailyClassType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

sealed class ClassesScreenEvent {
    object ToggleCreateClassesSection : ClassesScreenEvent()
    object GenerateClassesTapped : ClassesScreenEvent()
    data class SelectedNewDate(val selectedDate: LocalDate?) : ClassesScreenEvent()
    class ClassChanged(val type: DailyClassType) : ClassesScreenEvent()
    class WeekdayChanged(val dayOfWeek: DayOfWeek) : ClassesScreenEvent()
    class StartTimeChanged(val startTime: LocalTime) : ClassesScreenEvent()
    class EndTimeChanged(val endTime: LocalTime) : ClassesScreenEvent()
}
