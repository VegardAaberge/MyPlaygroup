package com.myplaygroup.app.core.data.mapper

import com.myplaygroup.app.feature_main.data.model.MonthlyPlanEntity
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan

fun MonthlyPlanEntity.toMonthlyPlan() : MonthlyPlan {

    return MonthlyPlan(
        id = id,
        username = username,
        kidName = kidName,
        month = month,
        year = year,
        paid = paid,
        planName = planName,
        daysOfWeek = daysOfWeek.map { x -> enumValueOf(x) },
        planPrice = planPrice,
        modified = modified
    )
}

fun MonthlyPlan.toMonthlyPlanEntity() : MonthlyPlanEntity {

    return MonthlyPlanEntity(
        id = id,
        username = username,
        kidName = kidName,
        month = month,
        year = year,
        paid = paid,
        planName = planName,
        daysOfWeek = daysOfWeek.map { x -> x.name },
        planPrice = planPrice,
        modified = modified
    )
}