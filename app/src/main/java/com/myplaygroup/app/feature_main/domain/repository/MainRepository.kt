package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getChatMessages(
        fetchFromRemote: Boolean
    ) : Flow<Resource<List<Message>>>

    fun ClearAllTables()
}