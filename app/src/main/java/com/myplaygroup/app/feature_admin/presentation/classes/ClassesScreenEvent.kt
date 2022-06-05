package com.myplaygroup.app.feature_admin.presentation.classes

import java.time.LocalDate

sealed class ClassesScreenEvent {
    data class SelectedNewDate(val selectedDate: LocalDate?) : ClassesScreenEvent()
    data class AddNewDateToCreate(val createDate: List<LocalDate>) : ClassesScreenEvent()
}
