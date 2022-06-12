package com.myplaygroup.app.core.data.mapper

import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.feature_main.data.model.MonthlyPlanEntity
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun MonthlyPlanEntity.toMonthlyPlan() : MonthlyPlan {

    val dateFormat = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT, Locale("en"))
    val parsedStartDate = LocalDate.parse(startDate, dateFormat) ?: LocalDate.now()
    val parsedEndDate = LocalDate.parse(endDate, dateFormat) ?: LocalDate.now()

    return MonthlyPlan(
        id = id,
        clientId = clientId,
        username = username,
        kidName = kidName,
        startDate = parsedStartDate,
        endDate = parsedEndDate,
        planName = planName,
        daysOfWeek = daysOfWeek.map { x -> enumValueOf(x) },
        planPrice = planPrice,
        cancelled = cancelled,
        modified = modified
    )
}

fun MonthlyPlan.toMonthlyPlanEntity() : MonthlyPlanEntity {

    val dateFormat = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT, Locale("en"))

    return MonthlyPlanEntity(
        id = id,
        clientId = clientId,
        username = username,
        kidName = kidName,
        startDate = startDate.format(dateFormat),
        endDate = endDate.format(dateFormat),
        planName = planName,
        daysOfWeek = daysOfWeek.map { x -> x.name },
        planPrice = planPrice,
        cancelled = cancelled,
        modified = modified
    )
}