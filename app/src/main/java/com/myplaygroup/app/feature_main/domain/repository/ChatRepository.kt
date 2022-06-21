package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun getChatMessages(
        fetchFromRemote: Boolean,
        allMessages: Boolean
    ) : Flow<Resource<List<Message>>>

    suspend fun getChatMessagesFromDB(
        users: List<String>
    ) : Resource<List<Message>>
}