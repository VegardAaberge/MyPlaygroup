package com.myplaygroup.app.feature_main.domain.interactors

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.model.ChatGroup
import com.myplaygroup.app.feature_main.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatInteractor {
    suspend fun getChatGroups(
        users: List<AppUser>,
        oldChatGroups: List<ChatGroup>
    ) : Flow<Resource<List<ChatGroup>>>

    suspend fun getChatMessages(
        user: String
    ) : Flow<Resource<List<Message>>>
}