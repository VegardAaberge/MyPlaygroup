package com.myplaygroup.app.feature_main.data.mapper

import com.myplaygroup.app.feature_main.data.model.MessageEntity
import com.myplaygroup.app.feature_main.data.remote.request.SendMessageRequest
import com.myplaygroup.app.feature_main.domain.model.Message
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

const val dateFormat = "yyyy-MM-dd'T'HH:mm:ss"

fun MessageEntity.toMessage(
    isSending: Boolean = false
) : Message {
    val dateFormat = DateTimeFormatter.ofPattern(dateFormat, Locale("en"))
    val parsedDate = LocalDateTime.parse(created, dateFormat) ?: LocalDateTime.now()

    return Message(
        id = id,
        clientId = clientId,
        message = message,
        createdBy = createdBy,
        created = parsedDate.toEpochSecond(ZoneOffset.UTC),
        profileName = profileName ?: "",
        isSending = isSending
    )
}

fun Message.toMessageEntity() : MessageEntity {
    val dateFormat = DateTimeFormatter.ofPattern(dateFormat, Locale("en"))
    val created = dateFormat.format(LocalDateTime.ofEpochSecond(created, 0, ZoneOffset.UTC))

    return MessageEntity(
        id = id,
        clientId = clientId,
        message = message,
        createdBy = createdBy,
        created = created,
        profileName = profileName
    )
}

fun MessageEntity.ToSendMessageRequest(receivers: List<String>) : String {
    return Json.encodeToString(
        SendMessageRequest(
            clientId = clientId,
            message = message,
            receivers = receivers
        )
    )
}