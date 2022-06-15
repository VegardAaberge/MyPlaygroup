package com.myplaygroup.app.feature_main.data.repository

import android.content.Context
import android.util.Log
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.settings.UserSettings
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.util.Constants
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

    private val dao = mainDatabase.mainDao()
    private var socket: WebSocketSession? = null
    private val sentMessages: HashSet<String> = HashSet()

    override suspend fun initSession(
        username: String,
        tryReconnect: Boolean
    ): Resource<Flow<Message>> {
        return try {
            if(!checkForInternetConnection(context)){
                return Resource.Error("No internet connection")
            }

            Log.d(Constants.DEBUG_KEY, "Init Session")
            socket = client.webSocketSession(
                urlString = ChatSocketRepository.Endpoints.ChatSocket.url,
            ){
                headers {
                    header("cookie", "Bearer " + authInterceptor.accessToken)
                }
            }

            return if(socket?.isActive == true){
                Log.d(Constants.DEBUG_KEY, "Observe Messages")
                observeMessages()
            }else if(tryReconnect){
                Log.d(Constants.DEBUG_KEY, "Reconnect Session")
                tryReconnect(username)
            }else{
                Log.d(Constants.DEBUG_KEY, "Failed to connect to websocket")
                Resource.Error("Failed to connect")
            }

        }catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    fun observeMessages(): Resource<Flow<Message>> {
        return try {
            val flow = socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    Log.i(Constants.DEBUG_KEY, "ObserveMessages: " + json)
                    val messageEntity = Json.decodeFromString<MessageEntity>(json)
                    dao.insertMessage(messageEntity)
                    sentMessages.removeAll { sm -> sm == messageEntity.clientId  }
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
            messageEntity = messageEntity,
            receivers = receivers,
            userSettings = userSettings
        )
    }

    override suspend fun sendMessage(
        messageEntity: MessageEntity,
        receivers: List<String>,
        userSettings: UserSettings
    ): Flow<Resource<Message>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                dao.insertMessage(messageEntity)
                sentMessages.add(messageEntity.clientId)
                emit(Resource.Success(messageEntity.toMessage(true)))

                if(!checkForInternetConnection(context)){
                    throw IOException("No internet connection")
                }

                if(socket == null || !socket!!.isActive){
                    val result = tryReconnect(userSettings.username)
                    if(socket == null || !socket!!.isActive){
                        throw IllegalStateException(result.message)
                    }
                }

                val sendMessageRequest = messageEntity.ToSendMessageRequest()
                socket?.send(Frame.Text(sendMessageRequest))

                withTimeout(30000){
                    while (sentMessages.contains(messageEntity.clientId)){
                        delay(100)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                val errorMessage = e.localizedMessage ?: "Unknown error"
                emit(Resource.Error(errorMessage, messageEntity.toMessage()))
            } finally {
                sentMessages.removeAll { sm -> sm == messageEntity.clientId  }
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

    private suspend fun tryReconnect(username: String) : Resource<Flow<Message>> {
        val result = tokenRepository.verifyRefreshToken()
        if(result is Resource.Success){
            return initSession(username, false)
        }else{
            return Resource.Error(result.message!!)
        }
    }
}