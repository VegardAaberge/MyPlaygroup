package com.myplaygroup.app.core.data.mapper

import com.myplaygroup.app.feature_main.data.model.MonthlyPlanEntity
import com.myplaygroup.app.feature_main.data.remote.response.items.MonthlyPlanItem
import com.myplaygroup.app.core.domain.model.MonthlyPlan

fun MonthlyPlanEntity.toMonthlyPlan() : MonthlyPlan {

    return MonthlyPlan(
        id = id,
        serverId = serverId,
        kidName = kidName,
        paid = paid,
        planName = planName,
        planPrice = planPrice
    )
}

fun MonthlyPlan.toMonthlyPlanEntity() : MonthlyPlanEntity {

    return MonthlyPlanEntity(
        id = id,
        serverId = serverId,
        kidName = kidName,
        paid = paid,
        planName = planName,
        planPrice = planPrice
    )
}

fun MonthlyPlanItem.toMonthlyPlanEntity() : MonthlyPlanEntity {

    return MonthlyPlanEntity(
        serverId = id,
        kidName = kidName,
        paid = paid,
        planName = planName,
        planPrice = planPrice
    )
}