package com.myplaygroup.app.feature_login.data.repository

import android.content.Context
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.fetchNetworkResource
import com.myplaygroup.app.feature_login.data.responses.LoginResponse
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
                        profileName = loginResponse.profile_name!!,
                        phoneNumber = loginResponse.phone_number!!,
                    )
                }
                loginResponse
            },
            onFetchError = { r ->
                when(r.code){
                    403 -> context.getString(R.string.error_wrong_username_or_password)
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
}


