package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Constants.LOCALHOST_SOCKET_URL
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatSocketRepository {

    suspend fun initSession(
        username: String
    ): Resource<String>

    suspend fun sendMessage(
        message: String,
        receivers: List<String>
    ): Resource<String>

    fun observeMessages(): Resource<Flow<Message>>

    suspend fun closeSession() : Resource<String>

    sealed class Endpoints(val url: String){
        object ChatSocket: Endpoints("$LOCALHOST_SOCKET_URL/chat-socket")
    }
}