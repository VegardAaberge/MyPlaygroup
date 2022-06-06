package com.myplaygroup.app.feature_main.presentation.admin.classes

import java.time.LocalDate

sealed class ClassesScreenEvent {
    object ToggleCreateClassesSection : ClassesScreenEvent()
    object GenerateClassesTapped : ClassesScreenEvent()
    data class SelectedNewDate(val selectedDate: LocalDate?) : ClassesScreenEvent()
}
