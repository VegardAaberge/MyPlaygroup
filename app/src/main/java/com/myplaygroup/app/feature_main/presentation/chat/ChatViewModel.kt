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
import kotlinx.coroutines.flow.*
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
                viewModelScope.launch {
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
                viewModelScope.launch {
                    val messageEntity = event.message.toMessageEntity()
                    val userSettings = userSettingsManager.getFlow().first()

                    socketRepository.sendMessage(
                        messageEntity = messageEntity,
                        receivers = receivers,
                        userSettings = userSettings
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

        viewModelScope.launch(Dispatchers.IO){
            val result = socketRepository.initSession(username)
            when(result){
                is Resource.Success -> {
                    observeMessages(result)
                }
                is Resource.Error -> {
                    setUIEvent(
                        UiEvent.ShowSnackbar(result.message ?: "Unknown error")
                    )
                }
                else -> {}
            }
        }
    }

    private fun collectGetMessages(result: Resource<List<Message>>) = viewModelScope.launch(Dispatchers.Main) {
        when (result) {
            is Resource.Success -> {
                state = state.copy(
                    messages = result.data!!,
                    showProgressIndicator = state.isLoading && result.data.isEmpty()
                )
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

    private fun observeMessages(result: Resource<Flow<Message>>) {
        result.data!!.onEach { message ->
            val newList = state.messages.toMutableList().apply {
                removeIf { u -> u.clientId == message.clientId }
                add(0, message)
            }
            state = state.copy(
                messages = newList
            )
        }.launchIn(viewModelScope)
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