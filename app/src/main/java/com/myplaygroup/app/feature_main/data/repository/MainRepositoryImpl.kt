package com.myplaygroup.app.feature_main.data.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.remote.MyPlaygroupApi
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.KEY_ACCESS_TOKEN
import com.myplaygroup.app.core.util.Constants.KEY_REFRESH_TOKEN
import com.myplaygroup.app.core.util.Constants.NO_VALUE
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.data.mapper.toMessage
import com.myplaygroup.app.feature_main.data.models.MessageEntity
import com.myplaygroup.app.feature_main.data.requests.MessageRequest
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.MainRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainRepositoryImpl @Inject constructor(
    private val api: MyPlaygroupApi,
    private val mainDatabase: MainDatabase,
    private val app: Application,
    private val sharedPreferences: SharedPreferences,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    @ApplicationContext private val context: Context
) : MainRepository {

    private val dao = mainDatabase.mainDao()

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
            },
            onFetchFailed = {
                verifyRefreshToken()
            }
        )
    }

    suspend fun verifyRefreshToken() : Boolean {
        if(basicAuthInterceptor.accessToken != null)
            return false

        val refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, NO_VALUE) ?: NO_VALUE;
        if(refreshToken == NO_VALUE)
            return false

        basicAuthInterceptor.accessToken = refreshToken

        val response = api.refreshToken()

        if(response.isSuccessful && response.body() != null){
            val body = response.body()!!

            basicAuthInterceptor.accessToken = body.access_token
            sharedPreferences.edit {
                putString(KEY_ACCESS_TOKEN, body.access_token)
                putString(KEY_REFRESH_TOKEN, body.refresh_token)
            }
        }else{
            basicAuthInterceptor.accessToken = null
            sharedPreferences.edit {
                putString(KEY_ACCESS_TOKEN, NO_VALUE)
                putString(KEY_REFRESH_TOKEN, NO_VALUE)
            }
        }

        return response.isSuccessful && response.body() != null
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun ClearAllTables() {
        GlobalScope.launch(Dispatchers.IO) {
            mainDatabase.clearAllTables()
        }
    }

    override suspend fun sendMessage(
        message: String,
        receivers: List<String>
    ): Flow<Resource<String>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                val response = api.sendMessage(
                    MessageRequest(
                        message = message,
                        receivers = receivers
                    )
                )

                if(response.isSuccessful && response.code() == 200 && response.body() != null){

                    val message = response.body()!!
                    dao.insertMessage(message)

                    emit(Resource.Success("Message sent"))
                }else{
                    emit(Resource.Error("Unknown error: ${response.message()}"))
                }

            }catch (e: Exception){
                Log.e(Constants.DEBUG_KEY, e.stackTraceToString())
                emit(Resource.Error(context.getString(R.string.api_login_exception)))
            }finally {
                emit(Resource.Loading(false))
            }
        }
    }
}