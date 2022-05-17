package com.myplaygroup.app.feature_profile.domain.repository

import android.net.Uri
import com.myplaygroup.app.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun createProfile(
        profileName: String,
        phoneNumber: String,
        email: String,
        newPassword: String,
        profileUri: Uri?
    ) : Flow<Resource<String>>

    suspend fun editProfile(
        profileName: String,
        phoneNumber: String,
        email: String,
    ) : Flow<Resource<String>>
}