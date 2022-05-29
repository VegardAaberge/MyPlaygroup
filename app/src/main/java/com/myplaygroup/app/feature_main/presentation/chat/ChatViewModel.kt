package com.myplaygroup.app.feature_main.presentation.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.Settings.UserSettingsManager
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.mapper.toMessageEntity
import com.myplaygroup.app.feature_main.data.repository.ChatSocketRepositoryImpl
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.MainRepository
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: MainRepository,
    private val socketRepository: ChatSocketRepositoryImpl,
    private val imageRepository: ImageRepository,
    private val userSettingsManager: UserSettingsManager
) : ViewModel() {

    lateinit var mainViewModel: MainViewModel

    var state by mutableStateOf(ChatState())

    fun onEvent(event: ChatScreenEvent){
        when(event){
            is ChatScreenEvent.EnteredNewMessage -> {
                state = state.copy(newMessage = event.newMessage)
            }
            is ChatScreenEvent.SendMessageTapped -> {
                viewModelScope.launch {
                    socketRepository.sendMessage(
                        message = state.newMessage,
                        receivers = listOf(mainViewModel.state.receiver)
                    ).collect{ collectInsertMessages(it) }
                }
            }
            is ChatScreenEvent.ConnectToChat -> {
                getMessages(true)
                connectToChat()
            }
            is ChatScreenEvent.DisconnectFromChat -> {
                viewModelScope.launch {
                    socketRepository.closeSession()
                }
            }
            is ChatScreenEvent.ResendMessage -> {
                getMessages(true)

                viewModelScope.launch {
                    val messageEntity = event.message.toMessageEntity()
                    val userSettings = userSettingsManager.getFlow().first()

                    socketRepository.sendMessage(
                        messageEntity = messageEntity,
                        receivers = listOf(mainViewModel.state.receiver),
                        userSettings = userSettings
                    ).collect{ collectInsertMessages(it) }
                }
            }
        }
    }

    private fun getMessages(
        fetchFromRemote: Boolean = false
    ){
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .getChatMessages(fetchFromRemote)
                .collect { collectGetMessages(it)}
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
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.ShowSnackbar(result.message!!)
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

    private fun connectToChat() = viewModelScope.launch {
        val result = socketRepository.initSession(mainViewModel.username.first())
        when(result){
            is Resource.Success -> {
                observeMessages(result)
            }
            is Resource.Error -> {
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.ShowSnackbar(result.message ?: "Unknown error")
                )
            }
            else -> {}
        }
    }

    private fun observeMessages(result: Resource<Flow<Message>>) {
        result.data!!.onEach { message ->
            val newList = state.messages.toMutableList().apply {
                removeIf { u -> u.id == message.id }
                add(0, message)
            }
            state = state.copy(
                messages = newList
            )
        }.launchIn(viewModelScope)
    }

    private fun collectInsertMessages(result: Resource<Message>) {
        when (result) {
            is Resource.Success -> {
                val newList = state.messages.toMutableList().apply {
                    add(0, result.data!!)
                }
                state = state.copy(
                    newMessage = "",
                    messages = newList
                )
            }
            is Resource.Error -> {
                val newMessages = result.data?.let {
                    state.messages.toMutableList().apply {
                        val message = find { m -> m.id == it.id }
                        message?.let { it.hasError = true }
                    }
                }
                newMessages?.let {
                    state = state.copy(
                        messages = it
                    )
                }
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.ShowSnackbar(result.message!!)
                )
            }
            is Resource.Loading -> {
                state = state.copy(
                    isLoading = result.isLoading
                )
            }
        }
    }
}