package com.myplaygroup.app.feature_main.presentation.admin.create_plans

import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.model.StandardPlan
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

data class CreatePlansState(
    val createMultipleUsers: Boolean = false,
    val users: List<AppUser> = emptyList(),
    val standardPlans: List<StandardPlan> = emptyList(),
    val monthlyPlans: List<MonthlyPlan> = emptyList(),
    val weekdays: Map<DayOfWeek, Boolean> = initWeekdays().toMap(),
    var user: String  = "",
    var kid: String = "",
    var plan: String = "",
    var price: String = "0",
    var startDate: LocalDate = LocalDate.now(),
    var endDate: LocalDate = LocalDate.now(),

    var multipleStartDate: LocalDate = LocalDate.now(),
    var multipleEndDate: LocalDate = LocalDate.now(),
    var baseMonthlyPlans: List<MonthlyPlan> = emptyList(),
    var basePlansSelected: Map<String, Boolean> = emptyMap(),

    val isLoading: Boolean = false
){
    companion object{
        private fun initWeekdays() : EnumMap<DayOfWeek, Boolean> {
            val weekdays : EnumMap<DayOfWeek, Boolean> = EnumMap(DayOfWeek::class.java)
            return weekdays
        }
    }
}
