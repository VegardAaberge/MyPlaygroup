package com.myplaygroup.app.feature_main.data.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class FakeMainRepository : ChatRepository {

    var messages = mutableListOf<Message>(
        Message(
            message = "Message 1",
            profileName = "Vegard",
            createdBy = "vegard",
            created = LocalDateTime.now(),
            isSending = false
        ),
        Message(
            message = "Message 2",
            profileName = "Playgroup",
            createdBy = "admin",
            created = LocalDateTime.now().plusSeconds(1),
            isSending = false
        ),
        Message(
            message = "Message 3",
            profileName = "Vegard",
            createdBy = "vegard",
            created = LocalDateTime.now().plusSeconds(2),
            isSending = false
        )
    )

    override suspend fun getChatMessages(fetchFromRemote: Boolean, b: Boolean): Flow<Resource<List<Message>>> {
        return flow { Resource.Success(messages) }
    }

    override suspend fun getChatMessagesFromDB(users: List<String>): Resource<List<Message>> {
        TODO("Not yet implemented")
    }
}