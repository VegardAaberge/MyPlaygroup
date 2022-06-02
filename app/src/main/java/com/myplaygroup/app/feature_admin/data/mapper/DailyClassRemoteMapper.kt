package com.myplaygroup.app.feature_admin.data.mapper

import com.myplaygroup.app.feature_admin.data.local.DailyClassEntity
import com.myplaygroup.app.feature_admin.data.remote.DailyClassItem
import com.myplaygroup.app.feature_admin.data.remote.DailyClassesResponse
import com.myplaygroup.app.feature_admin.domain.model.DailyClass
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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