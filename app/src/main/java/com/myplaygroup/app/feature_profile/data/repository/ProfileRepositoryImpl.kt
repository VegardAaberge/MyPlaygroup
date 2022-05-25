package com.myplaygroup.app.feature_profile.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.KEY_EMAIL
import com.myplaygroup.app.core.util.Constants.KEY_PHONE_NUMBER
import com.myplaygroup.app.core.util.Constants.KEY_PROFILE_NAME
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.fetchNetworkResource
import com.myplaygroup.app.feature_profile.data.requests.ProfileRequest
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalStateException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val tokenRepository: TokenRepository,
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context
) : ProfileRepository {

    override suspend fun createProfile(
        profileName: String,
        phoneNumber: String,
        email: String,
        newPassword: String,
        profileUri: Uri?
    ): Flow<Resource<Unit>> {

        val username = sharedPreferences.getString(Constants.KEY_USERNAME, Constants.NO_VALUE) ?: Constants.NO_VALUE

        return fetchNetworkResource(
            fetch = {
                api.createProfile(
                    username,
                    ProfileRequest(
                        profileName = profileName,
                        phoneNumber = phoneNumber,
                        email = email,
                        password = newPassword
                    )
                )
            },
            processFetch = { body ->
                sharedPreferences.edit {
                    putString(KEY_PROFILE_NAME, body.profileName)
                    putString(KEY_PHONE_NUMBER, body.phoneNumber)
                    putString(KEY_EMAIL, body.email)
                    apply()
                }
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

    override suspend fun editProfile(
        profileName: String,
        phoneNumber: String,
        email: String
    ): Flow<Resource<Unit>> {

        val username = sharedPreferences.getString(Constants.KEY_USERNAME, Constants.NO_VALUE) ?: Constants.NO_VALUE

        return fetchNetworkResource(
            fetch = {
                api.editProfile(
                    username = username,
                    ProfileRequest(
                        profileName = profileName,
                        phoneNumber = phoneNumber,
                        email = email
                    )
                )
            },
            processFetch = { body ->
                sharedPreferences.edit {
                    putString(KEY_PROFILE_NAME, body.profileName)
                    putString(KEY_PHONE_NUMBER, body.phoneNumber)
                    putString(KEY_EMAIL, body.email)
                    apply()
                }
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
}