package com.myplaygroup.app.feature_main.data.repository

import android.app.Application
import com.myplaygroup.app.core.data.remote.MyPlaygroupApi
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDao
import com.myplaygroup.app.feature_main.data.mapper.toMessage
import com.myplaygroup.app.feature_main.data.models.MessageEntity
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainRepositoryImpl @Inject constructor(
    private val api: MyPlaygroupApi,
    private val dao: MainDao,
    private val app: Application
) : MainRepository {

    override suspend fun getChatMessages(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Message>>> {
        return networkBoundResource<List<Message>, List<MessageEntity>>(
            query = {
                dao.getMessages().map { it.toMessage() }
            },
            fetch = {
                api.getMessages()
            },
            saveFetchResult = { comments ->
                dao.clearComments()
                dao.insertMessages(
                    comments.map { it }
                )
            },
            shouldFetch = {
                checkForInternetConnection(app)
            }
        )
    }
}