package com.myplaygroup.app.feature_main.data.mapper

import com.myplaygroup.app.feature_main.data.local.MessageEntity
import com.myplaygroup.app.feature_main.data.remote.MessageResponse

fun MessageResponse.toMessageEntity() : MessageEntity {
    return MessageEntity(
        serverId = id,
        message = message,
        createdBy = createdBy,
        created = created,
        profileName = profileName ?: "NO PROFILE NAME"
    )
}

fun MessageEntity.toMessageResponseEntity() : MessageResponse {
    return MessageResponse(
        id = serverId,
        message = message,
        createdBy = createdBy,
        created = created,
        profileName = profileName
    )
}