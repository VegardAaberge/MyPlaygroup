package com.myplaygroup.app.feature_main.data.repository

import android.content.Context
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.data.mapper.toMessage
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.ChatRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val mainDatabase: MainDatabase,
    private val tokenRepository: TokenRepository,
    @ApplicationContext private val context: Context
) : ChatRepository {

    private val dao = mainDatabase.mainDao()

    override suspend fun getChatMessages(
        fetchFromRemote: Boolean,
        allMessages: Boolean
    ): Flow<Resource<List<Message>>> {
        return networkBoundResource(
            query = {
                dao.getMessages().map { it.toMessage() }
            },
            fetch = {
                if (allMessages) {
                    api.getMessagesForAdmin()
                } else {
                    api.getMessagesForUser()
                }
            },
            saveFetchResult = { messages ->
                dao.clearSyncedComments()
                dao.insertMessages(messages)
                dao.getMessages().map { it.toMessage() }
            },
            shouldFetch = {
                fetchFromRemote && checkForInternetConnection(context)
            },
            onFetchError = { r ->
                when(r.code){
                    403 -> tokenRepository.verifyRefreshTokenAndReturnMessage()
                    else -> context.getString(R.string.error_could_not_reach_server, r.message)
                }
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> context.getString(R.string.error_no_internet_connection)
                    else -> {
                        t.localizedMessage?.let {
                            context.getString(R.string.error_server_exception, t.localizedMessage)
                        } ?: context.getString(R.string.error_unknown_error)
                    }
                }
            }
        )
    }

    override suspend fun getChatMessagesFromDB(users: List<String>): Resource<List<Message>> {
        return try {
            val messageEntities = dao.getMessagesForUser(users.first())
            val messages = messageEntities.map { it.toMessage() }
            Resource.Success(messages)
        } catch (t: Throwable) {
            t.printStackTrace()
            Resource.Error(t.localizedMessage ?: context.getString(R.string.error_unknown_error))
        }
    }
}