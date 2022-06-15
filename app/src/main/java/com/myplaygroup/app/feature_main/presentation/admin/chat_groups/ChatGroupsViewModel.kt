package com.myplaygroup.app.feature_main.presentation.admin.chat_groups

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.interactors.ChatInteractor
import com.myplaygroup.app.feature_main.domain.model.ChatGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatGroupsViewModel @Inject constructor(
    private val chatInteractor: ChatInteractor,
    private val imageRepository: ImageRepository
) : BaseViewModel() {

    var state by mutableStateOf(ChatGroupsState())

    init {
        getChatMessages()
    }

    fun onEvent(event: ChatGroupsScreenEvent){

    }

    fun getChatMessages() = viewModelScope.launch (Dispatchers.IO){
        chatInteractor.getChatGroups()
            .collect { collectChatGroups(it) }
    }

    private fun collectChatGroups(result: Resource<List<ChatGroup>>) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                state = state.copy(
                    chatGroups = result.data!!
                )
                state.chatGroups.toList().forEach { chatGroup ->
                    loadIcon(chatGroup)
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

    fun loadIcon(chatGroup: ChatGroup) = viewModelScope.launch(Dispatchers.IO)  {
        val imageResult = imageRepository.getProfileImage(chatGroup.username)
        if (imageResult is Resource.Success) {
            saveIcon(
                icon = imageResult.data!!,
                chatGroup = chatGroup
            )
        }
    }

    fun saveIcon(icon: Uri, chatGroup: ChatGroup) = viewModelScope.launch(Dispatchers.Main){

        val chatGroups = state.chatGroups.toMutableList()
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
