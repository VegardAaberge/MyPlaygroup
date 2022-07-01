package com.myplaygroup.app.feature_main.presentation.chat

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.mapper.toMessageEntity
import com.myplaygroup.app.feature_main.data.repository.ChatSocketRepositoryImpl
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val socketRepository: ChatSocketRepositoryImpl,
    private val userSettingsManager: UserSettingsManager,
    private var imageRepository: ImageRepository
) : BaseViewModel() {

    var state by mutableStateOf(ChatState())

    val username = userSettingsManager.getFlow {
        it.map { u -> u.username }
    }

    var receivers : List<String> = emptyList()
    var isAdmin : Boolean = false

    fun init(receiver: List<String>, isAdmin: Boolean) {
        this.receivers = receiver
        this.isAdmin = isAdmin

        viewModelScope.launch {
            val users = receivers.toMutableList()
            users.add(username.first())

            users.forEach { user ->
                loadProfileImage(user){ result ->
                    result.data?.let { uri ->
                        val userUri = state.userUri.toMutableMap()
                        userUri[user] = uri
                        state = state.copy(
                            userUri = userUri
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: ChatScreenEvent){
        when(event){
            is ChatScreenEvent.EnteredNewMessage -> {
                state = state.copy(newMessage = event.newMessage)
            }
            is ChatScreenEvent.SendMessageTapped -> {
                viewModelScope.launch(Dispatchers.IO) {
                    socketRepository.sendMessage(
                        message = state.newMessage,
                        receivers = receivers
                    ).collect{ collectInsertMessages(it) }
                }
            }
            is ChatScreenEvent.ConnectToChat -> {
                refreshChat(event.username)
            }
            is ChatScreenEvent.DisconnectFromChat -> {
                viewModelScope.launch {
                    socketRepository.closeSession()
                }
            }
            is ChatScreenEvent.ResendMessage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val messageEntity = event.message.toMessageEntity()
                    val username = username.first()

                    socketRepository.sendMessage(
                        newMessage = messageEntity,
                        receivers = receivers,
                        username = username
                    ).collect{ collectInsertMessages(it) }
                }
            }
        }
    }

    private fun refreshChat(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if(isAdmin){
                val result = repository.getChatMessagesFromDB(receivers)
                collectGetMessages(result)
            }else{
                repository
                    .getChatMessages(true, false)
                    .collect { collectGetMessages(it)}
            }

        }

        if(!socketRepository.isSocketActive()){
            viewModelScope.launch(Dispatchers.IO){
                val result = socketRepository.initSession(username, receivers)
                when(result){
                    is Resource.Success -> {
                        result.data!!.collect {
                            collectObservedMessages(it)
                        }
                    }
                    is Resource.Error -> {
                        socketRepository.closeSession()
                        setUIEvent(
                            UiEvent.ShowSnackbar(result.message!!)
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    private fun collectGetMessages(result: Resource<List<Message>>) = viewModelScope.launch(Dispatchers.Main) {
        when (result) {
            is Resource.Success -> {
                val messages = result.data!!
                val newMessages = messages.filter { m -> state.messages.all { it.clientId != m.clientId } }
                state = state.copy(
                    lastReadMessage = getLastReadMessage(messages),
                    messages = messages,
                    showProgressIndicator = state.isLoading && messages.isEmpty()
                )
                setMessagesAsRead(newMessages)
            }
            is Resource.Error -> {
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
            is Resource.Loading -> {
                state = state.copy(
                    isLoading = result.isLoading,
                    showProgressIndicator = result.isLoading
                )
            }
        }
    }

    private fun setMessagesAsRead(messages: List<Message>) = viewModelScope.launch {
        val username = username.first()
        val notReadMessages = messages.filter {
            it.createdBy != username && !it.readBy.contains(username)
        }

        notReadMessages.forEach { message ->
            val readBy = message.readBy.toMutableList()
            readBy.add(username)
            val readMessage = message.copy(
                readBy = readBy
            )

            viewModelScope.launch(Dispatchers.IO) {
                socketRepository.sendMessage(
                    newMessage = readMessage.toMessageEntity(),
                    receivers = receivers,
                    username = username
                ).collect()
            }
        }
    }

    private fun getLastReadMessage(messages: List<Message>) : List<ChatState.LastRead> {
        if(!isAdmin)
            return emptyList()

        return receivers.map { receiver ->
            val message = messages.filter { it.readBy.contains(receiver) }.maxByOrNull { it.created }
            message?.let {
                ChatState.LastRead(
                    messageClientId = message.clientId,
                    profileName = receiver
                )
            }
        }.filterNotNull()
    }

    private fun collectObservedMessages(message: Message) = viewModelScope.launch(Dispatchers.Main) {
        val newList = state.messages.toMutableList().apply {
            val index = indexOfFirst { it.clientId == message.clientId }
            removeIf { u -> u.clientId == message.clientId }
            if(index < 0){
                add(0, message)
            }else{
                add(index, message)
            }
        }
        val messageExist = state.messages.any { it.clientId == message.clientId }

        state = state.copy(
            lastReadMessage = getLastReadMessage(newList),
            messages = newList
        )

        if(!messageExist){
            setMessagesAsRead(listOf(message))
        }
    }

    private fun loadProfileImage(
        user: String,
        setState: (Resource<Uri?>) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {

        val result = imageRepository.getProfileImage(
            user = user
        )

        if (result is Resource.Success) {
            launch(Dispatchers.Main) {
                setState(result)
            }
        } else if (result is Resource.Error) {
            setUIEvent(
                UiEvent.ShowSnackbar(result.message!!)
            )
        }
    }

    private fun collectInsertMessages(result: Resource<Message>) = viewModelScope.launch(Dispatchers.Main) {
        when (result) {
            is Resource.Success -> {
                val message = result.data!!
                val newList = state.messages.toMutableList().apply {
                    removeIf { u -> u.clientId == message.clientId }
                    add(0, message)
                }
                state = state.copy(
                    newMessage = "",
                    messages = newList
                )
            }
            is Resource.Error -> {
                val newMessages = result.data?.let {
                    state.messages.toMutableList().apply {
                        val message = find { m -> m.clientId == it.clientId }
                        message?.let { it.isSending = false }
                    }
                }
                newMessages?.let {
                    state = state.copy(
                        messages = it
                    )
                }
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
                refreshChat(username.first())
            }
            is Resource.Loading -> {
                state = state.copy(
                    isLoading = result.isLoading
                )
            }
        }
    }
}