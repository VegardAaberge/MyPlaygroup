package com.myplaygroup.app.core.data.mapper

import com.myplaygroup.app.core.data.local.DailyClassEntity
import com.myplaygroup.app.core.data.remote.responses.DailyClassItem

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