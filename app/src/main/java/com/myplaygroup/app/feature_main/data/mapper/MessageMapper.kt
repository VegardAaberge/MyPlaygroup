package com.myplaygroup.app.feature_main.data.mapper

import android.icu.util.LocaleData
import com.myplaygroup.app.feature_main.data.models.MessageEntity
import com.myplaygroup.app.feature_main.domain.model.Message
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

const val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"

fun MessageEntity.toMessage() : Message {
    val dateFormat = DateTimeFormatter.ofPattern(dateFormat, Locale("en"))
    val parsedDate = LocalDateTime.parse(created, dateFormat) ?: LocalDateTime.now()

    return Message(
        message = message,
        createdBy = createdBy,
        created = parsedDate.toEpochSecond(ZoneOffset.UTC),
        profileName = profileName ?: "NO PROFILE NAME"
    )
}

fun Message.toMessageEntity() : MessageEntity {
    val dateFormat = DateTimeFormatter.ofPattern(dateFormat, Locale("en"))
    val created = dateFormat.format(LocalDateTime.ofEpochSecond(created, 0, ZoneOffset.UTC))

    return MessageEntity(
        message = message,
        createdBy = createdBy,
        created = created,
        profileName = profileName
    )
}