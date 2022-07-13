package com.myplaygroup.app.feature_main.data.mapper

import com.myplaygroup.app.feature_main.data.model.StandardPlanEntity
import com.myplaygroup.app.feature_main.domain.model.StandardPlan

fun StandardPlanEntity.toStandardPlan() : StandardPlan {

    return StandardPlan(
        name = name,
        price = price,
        type = type
    )
}