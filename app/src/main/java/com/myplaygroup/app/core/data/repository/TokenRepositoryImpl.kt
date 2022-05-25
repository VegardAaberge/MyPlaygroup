package com.myplaygroup.app.core.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.fetchApi
import com.myplaygroup.app.feature_login.data.responses.RefreshTokenResponse
import java.io.IOException
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val sharedPreferences: SharedPreferences
) : TokenRepository {

    override suspend fun verifyRefreshToken() : String {
        if(basicAuthInterceptor.accessToken != null)
            return "Couldn't reach server: Access token is null"

        val refreshToken = sharedPreferences.getString(
            Constants.KEY_REFRESH_TOKEN,
            Constants.NO_VALUE
        ) ?: Constants.NO_VALUE;

        if(refreshToken == Constants.NO_VALUE)
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

    private fun SetAccessToken(body: RefreshTokenResponse?){
        basicAuthInterceptor.accessToken = body?.access_token
        sharedPreferences.edit {
            putString(Constants.KEY_ACCESS_TOKEN, body?.access_token ?: Constants.NO_VALUE)
            putString(Constants.KEY_REFRESH_TOKEN, body?.refresh_token ?: Constants.NO_VALUE)
        }
    }
}