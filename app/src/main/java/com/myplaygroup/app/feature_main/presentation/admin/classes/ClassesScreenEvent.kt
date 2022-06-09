package com.myplaygroup.app.feature_main.presentation.admin.classes

import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.model.DailyClassType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

sealed class ClassesScreenEvent {
    object ToggleCreateClassesSection : ClassesScreenEvent()
    object GenerateClassesTapped : ClassesScreenEvent()
    object UploadCreatedClasses : ClassesScreenEvent()
    data class SelectedNewDate(val selectedDate: LocalDate?) : ClassesScreenEvent()
    data class ClassChanged(val type: DailyClassType) : ClassesScreenEvent()
    data class WeekdayChanged(val dayOfWeek: DayOfWeek) : ClassesScreenEvent()
    data class StartTimeChanged(val startTime: LocalTime) : ClassesScreenEvent()
    data class EndTimeChanged(val endTime: LocalTime) : ClassesScreenEvent()
    data class ClassSelected(val dailyClass: DailyClass?) : ClassesScreenEvent()
    data class SubmitSelectedClassTapped(
        val startTime: LocalTime,
        val endTime: LocalTime,
        val classDate: LocalDate,
        val delete: Boolean
    ) : ClassesScreenEvent()
}
