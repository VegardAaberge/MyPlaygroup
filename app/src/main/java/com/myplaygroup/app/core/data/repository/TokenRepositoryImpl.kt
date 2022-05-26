package com.myplaygroup.app.core.data.repository

import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.Settings.UserSettingsManager
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.NO_VALUE
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.fetchApi
import com.myplaygroup.app.feature_login.data.responses.RefreshTokenResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val userSettingsManager: UserSettingsManager
) : TokenRepository {

    override suspend fun verifyRefreshToken() : String {
        if(basicAuthInterceptor.accessToken != null)
            return "Couldn't reach server: Access token is null"

        val refreshToken = userSettingsManager.getFlow { it.map { u -> u.refreshToken }}.first()
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

    private suspend fun SetAccessToken(body: RefreshTokenResponse?){
        basicAuthInterceptor.accessToken = body?.access_token
        userSettingsManager.updateTokens(
            accessToken = body?.access_token ?: NO_VALUE,
            refreshToken = body?.refresh_token ?: NO_VALUE
        )
    }
}