package com.myplaygroup.app.feature_main.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*
import kotlin.math.roundToLong

data class MonthlyPlan(
    val clientId: String = UUID.randomUUID().toString(),
    val id: Long = -1,
    val username: String,
    val kidName: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val planName: String,
    val daysOfWeek: List<DayOfWeek>,
    val planPrice: Long,
    val availableClasses: Int = 1,
    val cancelledClasses: Int = 0,
    val changeDays: Boolean = false,
    val cancelled: Boolean = false,
    val modified: Boolean = id == -1L
){
    fun getAdjustedPlanPrice() : Long {
        val cancelledPercentage = (availableClasses - cancelledClasses) / (availableClasses.toFloat())
        return (planPrice * cancelledPercentage).roundToLong()
    }
}
