package com.myplaygroup.app.feature_main.data.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.mapper.toMessage
import com.myplaygroup.app.feature_main.data.mapper.toMessageEntity
import com.myplaygroup.app.feature_main.data.remote.MessageResponse
import com.myplaygroup.app.feature_main.data.remote.SendMessageRequest
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.ChatSocketRepository
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.NullPointerException
import javax.inject.Inject

class ChatSocketRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : ChatSocketRepository {

    private var socket: WebSocketSession? = null;

    override suspend fun initSession(username: String): Resource<String> {
        return try {
            socket = client.webSocketSession {
                url("${ChatSocketRepository.Endpoints.ChatSocket.url}?username=$username")
            }
            if(socket?.isActive == true){
                Resource.Success("Established connection")
            }else return Resource.Error("Couldn't establish a connection");

        }catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun sendMessage(message: String, receivers: List<String>): Resource<String> {
        return try {
            val requestMessage = Json.encodeToString(SendMessageRequest(message))
            socket?.send(Frame.Text(requestMessage))
            Resource.Success("Sent message")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override fun observeMessages(): Resource<Flow<Message>> {
        return try {
            val flow = socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messageResponse = Json.decodeFromString<MessageResponse>(json)
                    messageResponse.toMessageEntity().toMessage()
                }

            if(flow == null){
                throw NullPointerException("Flow can't be null");
            }
            Resource.Success(flow)

        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun closeSession(): Resource<String> {
        return try {
            socket?.close()
            Resource.Success("Closed Session")
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }
}