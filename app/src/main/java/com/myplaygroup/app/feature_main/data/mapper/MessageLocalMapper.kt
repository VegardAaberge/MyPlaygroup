package com.myplaygroup.app.feature_main.data.mapper

import com.myplaygroup.app.feature_main.data.local.MessageEntity
import com.myplaygroup.app.feature_main.domain.model.Message
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

const val dateFormat = "yyyy-MM-dd'T'HH:mm:ss"

fun MessageEntity.toMessage() : Message {
    val dateFormat = DateTimeFormatter.ofPattern(dateFormat, Locale("en"))
    val parsedDate = LocalDateTime.parse(created, dateFormat) ?: LocalDateTime.now()

    return Message(
        serverId = serverId,
        message = message,
        createdBy = createdBy,
        created = parsedDate.toEpochSecond(ZoneOffset.UTC),
        profileName = profileName
    )
}

fun Message.toMessageEntity() : MessageEntity {
    val dateFormat = DateTimeFormatter.ofPattern(dateFormat, Locale("en"))
    val created = dateFormat.format(LocalDateTime.ofEpochSecond(created, 0, ZoneOffset.UTC))

    return MessageEntity(
        serverId = serverId,
        message = message,
        createdBy = createdBy,
        created = created,
        profileName = profileName
    )
}