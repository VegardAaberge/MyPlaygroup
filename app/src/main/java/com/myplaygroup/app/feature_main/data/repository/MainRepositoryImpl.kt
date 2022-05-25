package com.myplaygroup.app.feature_main.data.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.util.*
import com.myplaygroup.app.core.util.Constants.KEY_ACCESS_TOKEN
import com.myplaygroup.app.core.util.Constants.KEY_REFRESH_TOKEN
import com.myplaygroup.app.core.util.Constants.NO_VALUE
import com.myplaygroup.app.feature_login.data.requests.SendEmailRequest
import com.myplaygroup.app.feature_login.data.responses.RefreshTokenResponse
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.data.mapper.toMessage
import com.myplaygroup.app.feature_main.data.local.MessageEntity
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
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
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
        return networkBoundResource(
            query = {
                dao.getMessages().map { it.toMessage() }
            },
            fetch = {
                api.getMessages()
            },
            saveFetchResult = { messages ->
                val comments = messages.map { it.toMessageEntity() }
                dao.clearComments()
                dao.insertMessages(comments)
                comments.map { it.toMessage() }
            },
            shouldFetch = {
                checkForInternetConnection(app)
            },
            onFetchError = { r ->
                when(r.code()){
                    403 -> verifyRefreshToken()
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

    suspend fun verifyRefreshToken() : String {
        if(basicAuthInterceptor.accessToken != null)
            return "Couldn't reach server: Access token is null"

        val refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, NO_VALUE) ?: NO_VALUE;
        if(refreshToken == NO_VALUE)
            return "Couldn't reach server: Refresh token is null"

        basicAuthInterceptor.accessToken = refreshToken

        val result = fetchApi(
            fetch = {
                api.refreshToken()
            },
            processFetch = { body ->
                SetAccessToken(body)
            },
            onFetchError = { r ->
                SetAccessToken(null)
                "Couldn't reach server: ${r.message()}"
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> "No Internet Connection"
                    else -> {
                        SetAccessToken(null)
                        "Server Exception: " + (t.localizedMessage ?: "Unknown exception")
                    }
                }
            }
        )

        return if(result is Resource.Success){
            Constants.AUTHENTICATION_ERROR_MESSAGE
        }else{
            result.message ?: "Unknown Error"
        }
    }

    fun SetAccessToken(body: RefreshTokenResponse?){
        basicAuthInterceptor.accessToken = body?.access_token
        sharedPreferences.edit {
            putString(KEY_ACCESS_TOKEN, body?.access_token ?: NO_VALUE)
            putString(KEY_REFRESH_TOKEN, body?.refresh_token ?: NO_VALUE)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun ClearAllTables() {
        GlobalScope.launch(Dispatchers.IO) {
            mainDatabase.clearAllTables()
        }
    }

    suspend fun sendMessage(
        message: String,
        receivers: List<String>
    ): Flow<Resource<String>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                val response = api.sendMessage(
                    SendMessageRequest(
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