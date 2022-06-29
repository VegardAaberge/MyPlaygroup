package com.myplaygroup.app.feature_profile.data.repository

import android.content.Context
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.fetchNetworkResource
import com.myplaygroup.app.feature_profile.data.requests.ProfileRequest
import com.myplaygroup.app.feature_profile.domain.model.EditProfileType
import com.myplaygroup.app.feature_profile.domain.model.EditProfileType.*
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val tokenRepository: TokenRepository,
    private val userSettingsManager: UserSettingsManager,
    @ApplicationContext private val context: Context
) : ProfileRepository {

    override suspend fun createProfile(
        username: String,
        profileName: String,
        phoneNumber: String,
        newPassword: String,
    ): Flow<Resource<Unit>> {

        return fetchNetworkResource(
            fetch = {
                api.createProfile(
                    username,
                    ProfileRequest(
                        profileName = profileName,
                        phoneNumber = phoneNumber,
                        password = newPassword,
                        editProfileType = NONE
                    )
                )
            },
            processFetch = { body ->
                userSettingsManager.updateProfileInfo(
                    profileName = body.profileName,
                    phoneNumber = body.phoneNumber,
                )
            },
            onFetchError = { r ->
                when(r.code){
                    403 -> tokenRepository.verifyRefreshTokenAndReturnMessage()
                    else -> "Couldn't reach server: ${r.message}"
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
        username: String,
        profileName: String,
        phoneNumber: String,
        newPassword: String,
        editProfileType: EditProfileType
    ): Flow<Resource<Unit>> {

        return fetchNetworkResource(
            fetch = {
                api.editProfile(
                    username = username,
                    ProfileRequest(
                        profileName = if(editProfileType == PROFILE_NAME) profileName else null,
                        phoneNumber = if(editProfileType == PHONE_NUMBER) phoneNumber else null,
                        password = if(editProfileType == PASSWORD) newPassword else null,
                        editProfileType = editProfileType
                    )
                )
            },
            processFetch = { body ->
                userSettingsManager.updateProfileInfo(
                    profileName = body.profileName,
                    phoneNumber = body.phoneNumber,
                )
            },
            onFetchError = { r ->
                when(r.code){
                    403 -> tokenRepository.verifyRefreshTokenAndReturnMessage()
                    else -> "Couldn't reach server: ${r.message}"
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