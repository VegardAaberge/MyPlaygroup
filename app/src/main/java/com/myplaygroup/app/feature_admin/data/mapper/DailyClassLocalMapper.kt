package com.myplaygroup.app.feature_admin.data.mapper

import com.myplaygroup.app.feature_admin.data.local.DailyClassEntity
import com.myplaygroup.app.core.domain.model.DailyClass
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

const val dateFormat = "yyyy-MM-dd"

fun DailyClassEntity.toDailyClass() : DailyClass {

    val dateFormat = DateTimeFormatter.ofPattern(dateFormat, Locale("en"))
    val parsedDate = LocalDate.parse(date, dateFormat) ?: LocalDate.now()

    return DailyClass(
        id = id,
        cancelled = cancelled,
        classType = classType,
        date = parsedDate,
        endTime = endTime,
        startTime = startTime
    )
}

fun DailyClass.toDailyClassEntity() : DailyClassEntity {

    val dateFormat = DateTimeFormatter.ofPattern(dateFormat, Locale("en"))

    return DailyClassEntity(
        id = id,
        cancelled = cancelled,
        classType = classType,
        date = date.format(dateFormat),
        endTime = endTime,
        startTime = startTime
    )
}