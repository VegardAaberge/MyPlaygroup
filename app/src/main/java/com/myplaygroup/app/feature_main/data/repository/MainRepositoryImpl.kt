package com.myplaygroup.app.feature_main.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.data.mapper.toMessage
import com.myplaygroup.app.feature_main.data.mapper.toMessageEntity
import com.myplaygroup.app.feature_main.data.remote.SendMessageRequest
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.MainRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val mainDatabase: MainDatabase,
    private val app: Application,
    private val tokenRepository: TokenRepository,
    @ApplicationContext private val context: Context
) : MainRepository {

    private val dao = mainDatabase.mainDao()

    override suspend fun getChatMessages(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Message>>> {
        return networkBoundResource(
            query = {
                dao.getMessages().map { it.toMessage() }.sortedByDescending { it.created }
            },
            fetch = {
                api.getMessages()
            },
            saveFetchResult = { messages ->
                val comments = messages.map { it.toMessageEntity() }
                dao.clearComments()
                dao.insertMessages(comments)
                comments.map { it.toMessage() }.sortedByDescending { it.created }
            },
            shouldFetch = {
                checkForInternetConnection(app)
            },
            onFetchError = { r ->
                when(r.code()){
                    403 -> tokenRepository.verifyRefreshToken()
                    else -> "Couldn't reach server: ${r.message()}"
                }
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> "No Internet Connection"
                    else -> "Server Exception: " + (t.localizedMessage ?: "Unknown exception")
                }
            }
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun clearAllTables() {
        GlobalScope.launch(Dispatchers.IO) {
            mainDatabase.clearAllTables()
        }
    }
}