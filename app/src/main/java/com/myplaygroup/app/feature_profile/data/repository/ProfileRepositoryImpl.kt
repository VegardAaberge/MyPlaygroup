package com.myplaygroup.app.feature_profile.data.repository

import android.content.Context
import android.net.Uri
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.Settings.UserSettingsManager
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.fetchNetworkResource
import com.myplaygroup.app.feature_profile.data.requests.ProfileRequest
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val tokenRepository: TokenRepository,
    private val userSettingsManager: UserSettingsManager,
    @ApplicationContext private val context: Context
) : ProfileRepository {

    override suspend fun createProfile(
        profileName: String,
        phoneNumber: String,
        email: String,
        newPassword: String,
        profileUri: Uri?
    ): Flow<Resource<Unit>> {

        val username = userSettingsManager.getFlow {
            it.map { u -> u.username }
        }.first()

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
                userSettingsManager.updateProfileInfo(
                    profileName = body.profileName,
                    phoneNumber = body.phoneNumber,
                    email = body.email,
                )
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

        val username = userSettingsManager.getFlow {
            it.map { u -> u.username }
        }.first()

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
                userSettingsManager.updateProfileInfo(
                    profileName = body.profileName,
                    phoneNumber = body.phoneNumber,
                    email = body.email,
                )
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