package com.myplaygroup.app.feature_main.presentation.chat

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.data.pref.UserSettings
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.NO_VALUE
import com.myplaygroup.app.core.util.Resource
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
    private val socketRepository: ChatSocketRepositoryImpl
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
                    val username = mainViewModel.username.first()
                    val receivers = if(username == "admin"){
                        listOf<String>("meng", "vegard")
                    }else{
                        listOf<String>("admin")
                    }

                    val response = socketRepository.sendMessage(
                        message = state.newMessage,
                        receivers = receivers
                    )
                    collectInsertMessages(response)
                }
            }
            is ChatScreenEvent.ConnectToChat -> {
                getMessages()
                connectToChat()
            }
            is ChatScreenEvent.DisconnectFromChat -> {
                viewModelScope.launch {
                    socketRepository.closeSession()
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
                    showProgressIndicator = state.isLoading && result.data!!.isEmpty()
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
                val observeResult = socketRepository.observeMessages()
                collectObserveMessages(observeResult)
            }
            is Resource.Error -> {
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.ShowSnackbar(result.message ?: "Unknown error")
                )
            }
            else -> {}
        }
    }

    private fun collectObserveMessages(observeResult: Resource<Flow<Message>>) {
        when(observeResult){
            is Resource.Success -> {
                observeMessages(observeResult)
            }
            is Resource.Error -> {
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.ShowSnackbar(observeResult.message ?: "Unknown error")
                )
            }
            else -> {}
        }
    }

    private fun observeMessages(result: Resource<Flow<Message>>) {
        result.data!!.onEach { message ->
            val newList = state.messages.toMutableList().apply {
                add(0, message)
            }
            state = state.copy(
                messages = newList
            )
        }.launchIn(viewModelScope)
    }

    private fun collectInsertMessages(result: Resource<String>) {
        when (result) {
            is Resource.Success -> {
                state = state.copy(newMessage = "")
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
}