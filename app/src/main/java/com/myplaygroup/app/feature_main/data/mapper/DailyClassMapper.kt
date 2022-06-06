package com.myplaygroup.app.core.data.mapper

import com.myplaygroup.app.feature_main.data.model.DailyClassEntity
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

const val dateFormat = "yyyy-MM-dd"
const val timeFormat = "HH:mm:ss"

fun DailyClassEntity.toDailyClass() : DailyClass {

    val dateFormat = DateTimeFormatter.ofPattern(dateFormat, Locale("en"))
    val parsedDate = LocalDate.parse(date, dateFormat) ?: LocalDate.now()

    val timeFormat = DateTimeFormatter.ofPattern(timeFormat, Locale("en"))
    val parsedStartTime = LocalTime.parse(startTime, timeFormat) ?: LocalTime.now()
    val parsedEndTime = LocalTime.parse(endTime, timeFormat) ?: LocalTime.now()

    return DailyClass(
        id = id,
        classType = classType,
        date = parsedDate,
        endTime = parsedEndTime,
        startTime = parsedStartTime,
        cancelled = cancelled,
        dayOfWeek = dayOfWeek,
    )
}

fun DailyClass.toDailyClassEntity() : DailyClassEntity {

    val dateFormat = DateTimeFormatter.ofPattern(dateFormat, Locale("en"))
    val timeFormat = DateTimeFormatter.ofPattern(timeFormat, Locale("en"))

    return DailyClassEntity(
        id = id,
        cancelled = cancelled,
        classType = classType,
        date = date.format(dateFormat),
        endTime = endTime.format(timeFormat),
        startTime = startTime.format(timeFormat),
        dayOfWeek = dayOfWeek,
    )
}