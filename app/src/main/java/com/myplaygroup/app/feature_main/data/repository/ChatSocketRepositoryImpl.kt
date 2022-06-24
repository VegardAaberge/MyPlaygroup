package com.myplaygroup.app.feature_main.data.repository

import android.content.Context
import android.util.Log
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.CHAT_SOCKET_URL
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.data.mapper.ToSendMessageRequest
import com.myplaygroup.app.feature_main.data.mapper.toMessage
import com.myplaygroup.app.feature_main.data.mapper.toMessageEntity
import com.myplaygroup.app.feature_main.data.model.MessageEntity
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.ChatSocketRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject

class ChatSocketRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val mainDatabase: MainDatabase,
    private val userSettingsManager: UserSettingsManager,
    private val authInterceptor: BasicAuthInterceptor,
    private val tokenRepository: TokenRepository,
    @ApplicationContext private val context: Context
) : ChatSocketRepository {

    private var receivedMessages: HashSet<LocalDateTime> = hashSetOf()
    private val dao = mainDatabase.mainDao()
    private var socket: WebSocketSession? = null
    private val sentMessages: HashSet<String> = HashSet()

    override fun isSocketActive(): Boolean {
        return socket?.isActive == true
    }

    override suspend fun initSession(
        username: String,
        receivers: List<String>,
        tryReconnect: Boolean
    ): Resource<Flow<Message>> {
        return try {
            if(!checkForInternetConnection(context)){
                return Resource.Error("No internet connection")
            }
            val url = CHAT_SOCKET_URL + receivers.joinToString(
                prefix = "?&listen=",
                separator = "&listen="
            )

            Log.d(Constants.DEBUG_KEY, "Init Session")
            socket = client.webSocketSession(
                urlString = url
            ){
                headers {
                    header("cookie", "Bearer " + authInterceptor.accessToken)
                }
            }

            return if(isSocketActive()){
                Log.d(Constants.DEBUG_KEY, "Observe Messages")
                observeMessages()
            }else if(tryReconnect){
                Log.d(Constants.DEBUG_KEY, "Reconnect Session")
                tryReconnect(username, receivers)
            }else{
                Log.d(Constants.DEBUG_KEY, "Failed to connect to websocket")
                Resource.Error("Failed to connect")
            }

        }catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    suspend private fun observeMessages(): Resource<Flow<Message>> {
        return try {
            val flow = socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    Log.i(Constants.DEBUG_KEY, "ObserveMessages: " + json)
                    val jsonEntity = Json.decodeFromString<MessageEntity>(json)
                    dao.insertMessage(jsonEntity)
                    sentMessages.removeAll { sm -> sm == jsonEntity.clientId  }

                    val messageEntity = dao.getMessageById(jsonEntity.clientId)
                    messageEntity.toMessage()
                }

            if(flow == null){
                throw NullPointerException("Flow can't be null")
            }
            Resource.Success(flow)

        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }


    override suspend fun sendMessage(
        message: String,
        receivers: List<String>
    ) : Flow<Resource<Message>> {
        val userSettings = userSettingsManager.getFlow().first()

        val messageEntity = Message(
            message = message,
            profileName = userSettings.profileName,
            createdBy = userSettings.username,
            created = LocalDateTime.now(),
            receivers = receivers
        ).toMessageEntity()

        return sendMessage(
            newMessage = messageEntity,
            receivers = receivers,
            username = userSettings.username
        )
    }

    override suspend fun sendMessage(
        newMessage: MessageEntity,
        receivers: List<String>,
        username: String
    ): Flow<Resource<Message>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                dao.insertMessage(newMessage)
                val messageEntity = dao.getMessageById(newMessage.clientId)
                sentMessages.add(messageEntity.clientId)
                emit(Resource.Success(messageEntity.toMessage(true)))

                if(!checkForInternetConnection(context)){
                    throw IOException("No internet connection")
                }

                if(socket == null || !socket!!.isActive){
                    val result = tryReconnect(username, receivers)
                    if(socket == null || !socket!!.isActive){
                        throw IllegalStateException(result.message)
                    }
                }

                val sendMessageRequest = messageEntity.ToSendMessageRequest()
                Log.d(Constants.DEBUG_KEY, "Sending message $sendMessageRequest")
                socket?.send(Frame.Text(sendMessageRequest))

                withTimeout(30000){
                    while (sentMessages.contains(messageEntity.clientId)){
                        delay(100)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                val errorMessage = e.localizedMessage ?: "Unknown error"
                emit(Resource.Error(errorMessage, newMessage.toMessage()))
            } finally {
                sentMessages.removeAll { sm -> sm == newMessage.clientId  }
                emit(Resource.Loading(false))
            }
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

    private suspend fun tryReconnect(username: String, receivers: List<String>) : Resource<Flow<Message>> {
        val result = tokenRepository.verifyRefreshToken()
        if(result is Resource.Success){
            return initSession(username, receivers,false)
        }else{
            return Resource.Error(result.message!!)
        }
    }
}