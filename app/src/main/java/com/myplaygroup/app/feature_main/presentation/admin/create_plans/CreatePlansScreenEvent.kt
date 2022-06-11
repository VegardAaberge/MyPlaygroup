package com.myplaygroup.app.feature_main.presentation.admin.create_plans

import java.time.DayOfWeek
import java.time.LocalDate

sealed class CreatePlansScreenEvent {
    object GenerateData : CreatePlansScreenEvent()
    data class WeekdayChanged(val dayOfWeek: DayOfWeek) : CreatePlansScreenEvent()
    data class UserChanged(val user: String) : CreatePlansScreenEvent()
    data class KidChanged(val kid: String) : CreatePlansScreenEvent()
    data class PlanChanged(val plan: String) : CreatePlansScreenEvent()
    data class PriceChanged(val price: String) : CreatePlansScreenEvent()
    data class StartDateChanged(val startDate: LocalDate) : CreatePlansScreenEvent()
    data class EndDateChanged(val endDate: LocalDate) : CreatePlansScreenEvent()
}