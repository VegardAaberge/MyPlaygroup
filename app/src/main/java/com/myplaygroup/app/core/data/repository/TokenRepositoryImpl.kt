package com.myplaygroup.app.core.data.repository

import android.util.Log
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.NO_VALUE
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.fetchApi
import com.myplaygroup.app.feature_login.data.responses.RefreshTokenResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val userSettingsManager: UserSettingsManager,
) : TokenRepository {

    private var hasMadeRequest : Boolean = false
    private var resetRequestResult : Resource<Unit>? = null

    override suspend fun verifyRefreshTokenAndReturnMessage(): String {
        val result = verifyRefreshToken()

        return if(result is Resource.Success){
            Constants.AUTHENTICATION_ERROR_MESSAGE
        }else{
            result.message!!
        }
    }

    override suspend fun verifyRefreshToken() : Resource<Unit> {

        // Wait for request to complete and return success
        if(hasMadeRequest){
            while (resetRequestResult == null){
                delay(10)
            }
            return Resource.Success()
        }

        // Wait 10 seconds before allowing another reset request
        hasMadeRequest = true
        withContext(NonCancellable) {
            delay(10000)
            hasMadeRequest = false
        }

        try {
            resetRequestResult = null
            resetRequestResult = verifyRefreshTokenBody()
        }catch(e: Exception) {
            resetRequestResult = Resource.Error("Reset token gave an exception")
            throw e;
        }

        return resetRequestResult ?: Resource.Error("Reset request result was null")
    }

    private suspend fun verifyRefreshTokenBody() : Resource<Unit> {
        val refreshToken = userSettingsManager.getFlow { it.map { u -> u.refreshToken }}.first()
        if(refreshToken == NO_VALUE)
            return Resource.Error("Couldn't reach server: Refresh token is null")

        Log.d(Constants.DEBUG_KEY, "Refresh_Token " + refreshToken)
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
            Resource.Success()
        }else{
            Resource.Error(result.message ?: "Unknown Error")
        }
    }

    private suspend fun SetAccessToken(body: RefreshTokenResponse?){
        basicAuthInterceptor.accessToken = body?.access_token
        userSettingsManager.updateTokens(
            accessToken = body?.access_token ?: NO_VALUE,
            refreshToken = body?.refresh_token ?: NO_VALUE
        )
    }
}