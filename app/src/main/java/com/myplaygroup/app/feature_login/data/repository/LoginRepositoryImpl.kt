package com.myplaygroup.app.feature_login.data.repository

import android.content.Context
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.fetchNetworkResource
import com.myplaygroup.app.feature_login.data.requests.SendEmailRequest
import com.myplaygroup.app.feature_login.data.requests.VerifyCodeRequest
import com.myplaygroup.app.feature_login.data.responses.LoginResponse
import com.myplaygroup.app.feature_login.data.responses.SendResetPasswordResponse
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
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
                if(checkForInternetConnection(context)){
                    api.login(
                        username = username,
                        password = password
                    )
                } else throw IOException()
            },
            processFetch = { loginResponse ->
                basicAuthInterceptor.accessToken = loginResponse.access_token
                userSettingsManager.updateUsernameAndRole(
                    username = username,
                    userRole = loginResponse.user_role,
                )
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
                if(checkForInternetConnection(context)){
                    api.sendEmailRequest(
                        SendEmailRequest(email)
                    )
                } else throw IOException()
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
                if(checkForInternetConnection(context)){
                    api.checkVerificationCode(
                        VerifyCodeRequest(
                            token = token,
                            code = code
                        )
                    )
                } else throw IOException()

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
}


