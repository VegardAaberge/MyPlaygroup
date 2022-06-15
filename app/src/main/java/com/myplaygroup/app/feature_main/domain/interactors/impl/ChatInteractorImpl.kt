package com.myplaygroup.app.feature_main.domain.interactors.impl

import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.local.MainDao
import com.myplaygroup.app.feature_main.data.model.AppUserEntity
import com.myplaygroup.app.feature_main.domain.interactors.ChatInteractor
import com.myplaygroup.app.feature_main.domain.model.ChatGroup
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatInteractorImpl @Inject constructor (
    private val chatRepository: ChatRepository,
    private val userSettingsManager: UserSettingsManager,
    private val dao: MainDao
) : ChatInteractor {

    override suspend fun getChatGroups(): Flow<Resource<List<ChatGroup>>> {

        return flow {
            emit(Resource.Loading(true))

            chatRepository.getChatMessages(true, true).collect{ result ->
                when(result){
                    is Resource.Success -> {
                        val chatGroups = getChatGroupFromMessages(result.data!!)
                        emit(chatGroups)
                    }
                    is Resource.Error -> {
                        emit(Resource.Error(result.message!!))
                    }
                    else -> {}
                }
            }

            emit(Resource.Loading(false))
        }
    }

    private suspend fun getChatGroupFromMessages(data: List<Message>): Resource<List<ChatGroup>> {
        if(data.size == 0)
            return Resource.Success(emptyList())

        val appUsersEntities = dao.getAppUsers()
        val username = userSettingsManager.getFlow { it.map { u -> u.username }}.first()

        val chatGroups = appUsersEntities
            .filter { x -> x.username != username }
            .map { appUser ->

                val lastMessage = data
                    .filter { message -> hasMessage(message, appUser, username) }
                    .maxByOrNull { x -> x.created }

                ChatGroup(
                    username = appUser.username,
                    lastMessage = lastMessage?.message,
                    updateTime = lastMessage?.created,
                )
            }

        return Resource.Success(chatGroups)
    }

    private fun hasMessage(message: Message, appUser: AppUserEntity, username: String) : Boolean {

        if(message.createdBy == appUser.username)
            return true

        if(message.createdBy == username && message.receivers.contains(appUser.username))
            return true

        return false
    }

    override suspend fun getChatMessages(user: String): Flow<Resource<List<Message>>> {
        TODO("Not yet implemented")
    }
}