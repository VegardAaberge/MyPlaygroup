package com.myplaygroup.app.feature_main.data.mapper

import com.myplaygroup.app.feature_main.data.models.MessageEntity
import com.myplaygroup.app.feature_main.domain.model.Message

fun MessageEntity.toMessage() : Message {
    return Message(
        message = message,
        owner = owner,
        receivers = receivers,
        created = created
    )
}

fun Message.toMessageEntity() : MessageEntity {
    return MessageEntity(
        message = message,
        owner = owner,
        receivers = receivers,
        created = created
    )
}