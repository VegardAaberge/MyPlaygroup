package com.myplaygroup.app.core.data.mapper

import com.myplaygroup.app.core.data.local.DailyClassEntity
import com.myplaygroup.app.core.data.local.MonthlyPlanEntity
import com.myplaygroup.app.core.data.remote.responses.MonthlyPlanItem
import com.myplaygroup.app.core.domain.model.DailyClass
import com.myplaygroup.app.core.domain.model.MonthlyPlan
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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