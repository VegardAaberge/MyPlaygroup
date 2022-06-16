package com.myplaygroup.app.feature_main.domain.interactors.impl

import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.local.MainDao
import com.myplaygroup.app.feature_main.domain.interactors.ChatInteractor
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.model.ChatGroup
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.ChatRepository
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatInteractorImpl @Inject constructor (
    private val chatRepository: ChatRepository,
    private val usersRepository: UsersRepository,
    private val userSettingsManager: UserSettingsManager,
    private val dao: MainDao
) : ChatInteractor {

    override suspend fun getChatGroups(users: List<AppUser>): Flow<Resource<List<ChatGroup>>> {

        return flow {
            emit(Resource.Loading(true))

            val username = userSettingsManager.getFlow { it.map { u -> u.username }}.first()
            emit(
                getChatGroupFromMessages(
                    users = users,
                    username = username
                )
            )

            chatRepository.getChatMessages(true, true).collect{ result ->
                when(result){
                    is Resource.Success -> {
                        emit(
                            getChatGroupFromMessages(
                                users = users,
                                username = username,
                                appUsers = result.data!!
                            )
                        )
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

    private suspend fun getChatGroupFromMessages(
        users: List<AppUser>,
        username: String,
        appUsers: List<Message> = emptyList()
    ): Resource<List<ChatGroup>> {

        val chatGroups = users
            .filter { x -> x.username != username }
            .map { appUser ->

                val lastMessage = appUsers
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

    private fun hasMessage(message: Message, appUser: AppUser, username: String) : Boolean {

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