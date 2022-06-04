package com.myplaygroup.app.feature_profile.domain.repository

import com.myplaygroup.app.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun createProfile(
        username: String,
        profileName: String,
        phoneNumber: String,
        email: String,
        newPassword: String,
    ) : Flow<Resource<Unit>>

    suspend fun editProfile(
        username: String,
        profileName: String,
        phoneNumber: String,
        email: String,
    ) : Flow<Resource<Unit>>
}