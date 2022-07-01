package com.myplaygroup.app.feature_main.presentation.admin.chat_groups

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.repository.ChatSocketRepositoryImpl
import com.myplaygroup.app.feature_main.domain.interactors.ChatInteractor
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.model.ChatGroup
import com.myplaygroup.app.feature_main.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatGroupsViewModel @Inject constructor(
    private val chatInteractor: ChatInteractor,
    private val imageRepository: ImageRepository,
    private val socketRepository: ChatSocketRepositoryImpl,
    private val userSettingsManager: UserSettingsManager
) : BaseViewModel() {

    private val profileImageLoading = mutableSetOf<String>()

    var state by mutableStateOf(ChatGroupsState())

    val username = userSettingsManager.getFlow {
        it.map { u -> u.username }
    }

    fun init(userFlow: MutableStateFlow<List<AppUser>>) {
        userFlow.onEach { users ->
            getChatMessages(users)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: ChatGroupsScreenEvent){
        when(event){
            is ChatGroupsScreenEvent.ConnectToChat -> {
                if(!socketRepository.isSocketActive()){
                    refreshChat(event.username)
                }
            }
            is ChatGroupsScreenEvent.DisconnectFromChat -> {
                viewModelScope.launch {
                    socketRepository.closeSession()
                }
            }
        }
    }

    private fun refreshChat(username: String) = viewModelScope.launch(Dispatchers.IO){
        val result = socketRepository.initSession(username, listOf("RECEIVE_ALL"))
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

    private fun collectObservedMessages(message: Message) = viewModelScope.launch(Dispatchers.Main) {
        val chatGroups = state.chatGroups.toMutableList()
        chatGroups.firstOrNull{ it.username == message.createdBy }?.let { group ->

            val newList = group.messages.toMutableList().apply {
                removeIf { u -> u.clientId == message.clientId }
                add(0, message)
            }

            val newChatGroup = group.copy(
                messages = newList,
                updateTime = message.created,
                lastMessage = message.message
            )
            val newChatGroups = chatGroups.apply {
                remove(group)
                add(0, newChatGroup)
            }
            state = state.copy(
                chatGroups = newChatGroups
            )
        }
    }

    private var previousUsers = emptyList<AppUser>()

    fun getChatMessages(users: List<AppUser>) = viewModelScope.launch (Dispatchers.IO){
        if(previousUsers == users)
            return@launch
        previousUsers = users

        chatInteractor.getChatGroups(users, state.chatGroups)
            .collect { collectChatGroups(it) }
    }

    private fun collectChatGroups(result: Resource<List<ChatGroup>>) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                state = state.copy(
                    chatGroups = result.data!!
                )
                state.chatGroups.toList().forEach { chatGroup ->
                    loadIcon(chatGroup.username)
                }
            }
            is Resource.Error -> {
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
            is Resource.Loading -> {
                isBusy(result.isLoading)
            }
        }
    }

    fun loadIcon(username: String) = viewModelScope.launch(Dispatchers.IO){
        if(profileImageLoading.contains(username))
            return@launch

        val chatGroup = state.chatGroups.firstOrNull { x -> x.username == username}
        if(chatGroup == null || chatGroup.icon != null)
            return@launch

        profileImageLoading.add(username)
        val imageResult = imageRepository.getProfileImage(username)
        profileImageLoading.remove(username)

        if (imageResult is Resource.Success) {
            saveIcon(
                icon = imageResult.data!!,
                username = username
            )
        }
    }

    fun saveIcon(icon: Uri, username: String) = viewModelScope.launch(Dispatchers.Main){

        val chatGroups = state.chatGroups.toMutableList()
        val chatGroup = chatGroups.firstOrNull { x -> x.username == username}
        if(chatGroup == null)
            return@launch

        val newChatGroup = chatGroup.copy(
            icon = icon
        )
        val index = chatGroups.indexOf(chatGroup)
        chatGroups[index] = newChatGroup

        state = state.copy(
            chatGroups = chatGroups
        )
    }
}
