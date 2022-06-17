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
    var weekdaysError: String? = null,
    var user: String  = "",
    var userError: String? = null,
    var kid: String = "",
    var kidError: String? = null,
    var plan: String = "",
    var planError: String? = null,
    var price: Int = 0,
    var priceError: String? = null,
    var startDate: LocalDate = LocalDate.now(),
    var startDateError: String? = null,
    var endDate: LocalDate = LocalDate.now(),
    var endDateError: String? = null,

    var multipleStartDate: LocalDate = LocalDate.now(),
    var multipleEndDate: LocalDate = LocalDate.now(),
    var baseMonthlyPlans: List<MonthlyPlan> = emptyList(),
    var basePlansSelected: Map<String, Boolean> = emptyMap(),

    val isLoading: Boolean = false
){
    companion object{
        private fun initWeekdays() : EnumMap<DayOfWeek, Boolean> {
            val weekdays : EnumMap<DayOfWeek, Boolean> = EnumMap(DayOfWeek::class.java)
            weekdays.put(DayOfWeek.MONDAY, false)
            weekdays.put(DayOfWeek.TUESDAY, false)
            weekdays.put(DayOfWeek.WEDNESDAY, false)
            weekdays.put(DayOfWeek.THURSDAY, false)
            weekdays.put(DayOfWeek.FRIDAY, false)
            weekdays.put(DayOfWeek.SATURDAY, false)
            return weekdays
        }
    }
}
