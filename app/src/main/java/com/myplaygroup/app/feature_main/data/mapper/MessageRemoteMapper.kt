package com.myplaygroup.app.feature_main.data.mapper

import com.myplaygroup.app.feature_main.data.local.MessageEntity
import com.myplaygroup.app.feature_main.data.remote.MessageResponse
import com.myplaygroup.app.feature_main.data.remote.SendMessageRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun MessageResponse.toMessageEntity() : MessageEntity {
    return MessageEntity(
        id = clientId,
        serverId = id,
        message = message,
        createdBy = createdBy,
        created = created,
        profileName = profileName ?: "",
        hasError = id == -1L // If it has no server id, it was not saved in the database
    )
}

fun MessageEntity.ToSendMessageRequest(receivers: List<String>) : String {
    return Json.encodeToString(SendMessageRequest(
        clientId = id,
        message = message,
        receivers = receivers
    ))
}