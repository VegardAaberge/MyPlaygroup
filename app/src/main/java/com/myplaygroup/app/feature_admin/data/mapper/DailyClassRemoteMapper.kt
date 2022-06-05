package com.myplaygroup.app.feature_admin.data.mapper

import com.myplaygroup.app.feature_admin.data.local.DailyClassEntity
import com.myplaygroup.app.feature_admin.data.remote.DailyClassItem

fun DailyClassItem.toDailyClassEntity() : DailyClassEntity {

    return DailyClassEntity(
        serverId = id,
        cancelled = cancelled,
        classType = classType,
        date = date,
        endTime = endTime,
        startTime = startTime
    )
}