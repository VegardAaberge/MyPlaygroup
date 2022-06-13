package com.myplaygroup.app.core.data.mapper

import com.myplaygroup.app.core.util.Constants.DATE_FORMAT
import com.myplaygroup.app.core.util.Constants.TIME_FORMAT
import com.myplaygroup.app.feature_main.data.model.DailyClassEntity
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

fun DailyClassEntity.toDailyClass() : DailyClass {

    val dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale("en"))
    val parsedDate = LocalDate.parse(date, dateFormat) ?: LocalDate.now()

    val timeFormat = DateTimeFormatter.ofPattern(TIME_FORMAT, Locale("en"))
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
        kids = kids,
        modified = modified
    )
}

fun DailyClass.toDailyClassEntity() : DailyClassEntity {

    val dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale("en"))
    val timeFormat = DateTimeFormatter.ofPattern(TIME_FORMAT, Locale("en"))

    return DailyClassEntity(
        id = id,
        cancelled = cancelled,
        classType = classType,
        date = date.format(dateFormat),
        endTime = endTime.format(timeFormat),
        startTime = startTime.format(timeFormat),
        dayOfWeek = dayOfWeek,
        kids = kids,
        modified = modified
    )
}