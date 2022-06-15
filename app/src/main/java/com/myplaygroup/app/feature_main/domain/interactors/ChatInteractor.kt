package com.myplaygroup.app.feature_main.domain.interactors

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.ChatGroup
import com.myplaygroup.app.feature_main.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatInteractor {
    suspend fun getChatGroups() : Flow<Resource<List<ChatGroup>>>

    suspend fun getChatMessages(
        user: String
    ) : Flow<Resource<List<Message>>>
}