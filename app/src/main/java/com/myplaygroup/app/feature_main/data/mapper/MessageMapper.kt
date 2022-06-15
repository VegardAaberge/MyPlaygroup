package com.myplaygroup.app.feature_main.data.mapper

import com.myplaygroup.app.core.util.Constants.DATE_TIME_FORMAT
import com.myplaygroup.app.feature_main.data.model.MessageEntity
import com.myplaygroup.app.feature_main.data.remote.request.SendMessageRequest
import com.myplaygroup.app.feature_main.domain.model.Message
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*



fun MessageEntity.toMessage(
    isSending: Boolean = false
) : Message {
    val dateFormat = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale("en"))
    val parsedDate = LocalDateTime.parse(created, dateFormat) ?: LocalDateTime.now()

    return Message(
        id = id,
        clientId = clientId,
        message = message,
        createdBy = createdBy,
        receivers = receivers,
        created = parsedDate,
        profileName = profileName ?: "",
        isSending = isSending,
    )
}

fun Message.toMessageEntity() : MessageEntity {
    val dateFormat = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale("en"))

    return MessageEntity(
        id = id,
        clientId = clientId,
        message = message,
        receivers = receivers,
        createdBy = createdBy,
        created = created.format(dateFormat),
        profileName = profileName
    )
}

fun MessageEntity.ToSendMessageRequest() : String {
    return Json.encodeToString(
        SendMessageRequest(
            clientId = clientId,
            message = message,
            receivers = receivers
        )
    )
}