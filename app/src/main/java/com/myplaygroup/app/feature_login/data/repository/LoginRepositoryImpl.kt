package com.myplaygroup.app.feature_login.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.Settings.UserSettingsManager
import com.myplaygroup.app.core.util.Constants.DEBUG_KEY
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.fetchNetworkResource
import com.myplaygroup.app.feature_login.data.requests.SendEmailRequest
import com.myplaygroup.app.feature_login.data.requests.VerifyCodeRequest
import com.myplaygroup.app.feature_login.data.responses.LoginResponse
import com.myplaygroup.app.feature_login.data.responses.SendResetPasswordResponse
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val userSettingsManager: UserSettingsManager,
    @ApplicationContext private val context: Context
) : LoginRepository {

    override suspend fun authenticate(
        username: String,
        password: String,
    ) : Flow<Resource<LoginResponse>> {

        return fetchNetworkResource(
            fetch = {
                api.login(
                    username = username,
                    password = password
                )
            },
            processFetch = { loginResponse ->
                basicAuthInterceptor.accessToken = loginResponse.access_token
                userSettingsManager.updateUsername(username)
                userSettingsManager.updateTokens(
                    accessToken = loginResponse.access_token,
                    refreshToken = loginResponse.refresh_token,
                )
                if(loginResponse.profile_created){
                    userSettingsManager.updateProfileInfo(
                        profileName = loginResponse.profile_name,
                        email = loginResponse.email,
                        phoneNumber = loginResponse.phone_number,
                    )
                }
                loginResponse
            },
            onFetchError = { r ->
                when(r.code()){
                    403 -> "Wrong username or password"
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

    override suspend fun sendEmailRequestForm(
        email: String
    ): Flow<Resource<SendResetPasswordResponse>> {

        return fetchNetworkResource(
            fetch = {
                api.sendEmailRequest(
                    SendEmailRequest(email)
                )
            },
            processFetch = { r -> r },
            onFetchError = { r -> "Couldn't reach server: ${r.message()}" },
            onFetchException = { t ->
                when(t){
                    is IOException -> "No Internet Connection"
                    else -> "Server Exception: " + (t.localizedMessage ?: "Unknown exception")
                }
            }
        )
    }

    override suspend fun checkVerificationCode(
        code: String,
        token: String
    ): Flow<Resource<String>> {

        return fetchNetworkResource(
            fetch = {
                api.checkVerificationCode(
                    VerifyCodeRequest(
                        token = token,
                        code = code
                    )
                )
            },
            processFetch = { r -> r.message },
            onFetchError = { r -> "Couldn't reach server: ${r.message()}" },
            onFetchException = { t ->
                when(t){
                    is IOException -> "No Internet Connection"
                    else -> "Server Exception: " + (t.localizedMessage ?: "Unknown exception")
                }
            }
        )
    }

    override suspend fun uploadProfileImage(uri: Uri?) : Flow<Resource<String>> {

        return flow {
            try {
                emit(Resource.Loading(true))
                // TODO upload image
            }catch (e: Exception){
                Log.e(DEBUG_KEY, e.stackTraceToString())
                emit(Resource.Error(context.getString(R.string.api_login_exception)))
            }finally {
                emit(Resource.Loading(false))
            }
        }
    }
}


