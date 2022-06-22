package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.data.settings.UserSettings
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.model.MessageEntity
import com.myplaygroup.app.feature_main.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatSocketRepository {

    fun isSocketActive(): Boolean

    suspend fun initSession(
        username: String,
        receivers: List<String>,
        tryReconnect: Boolean = true
    ): Resource<Flow<Message>>

    suspend fun sendMessage(
        message: String,
        receivers: List<String>
    ): Flow<Resource<Message>>

    suspend fun sendMessage(
        messageEntity: MessageEntity,
        receivers: List<String>,
        userSettings: UserSettings
    ): Flow<Resource<Message>>

    suspend fun closeSession() : Resource<String>
}