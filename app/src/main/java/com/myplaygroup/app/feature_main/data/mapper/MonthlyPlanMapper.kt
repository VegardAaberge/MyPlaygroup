package com.myplaygroup.app.core.data.mapper

import com.myplaygroup.app.feature_main.data.model.MonthlyPlanEntity
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan

fun MonthlyPlanEntity.toMonthlyPlan() : MonthlyPlan {

    return MonthlyPlan(
        id = id,
        kidName = kidName,
        paid = paid,
        planName = planName,
        planPrice = planPrice
    )
}

fun MonthlyPlan.toMonthlyPlanEntity() : MonthlyPlanEntity {

    return MonthlyPlanEntity(
        id = id,
        kidName = kidName,
        paid = paid,
        planName = planName,
        planPrice = planPrice
    )
}