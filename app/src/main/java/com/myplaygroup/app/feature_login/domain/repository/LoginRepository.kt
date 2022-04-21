package com.myplaygroup.app.feature_login.domain.repository

import android.net.Uri
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_login.data.remote.responses.LoginResponse
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun authenticate(username: String, password: String) : Flow<Resource<LoginResponse>>

    suspend fun sendEmailRequestForm(email: String): Flow<Resource<String>>

    suspend fun checkVerificationCode(code: String): Flow<Resource<String>>

    suspend fun createProfile(
        profileName: String,
        phoneNumber: String,
        email: String,
        newPassword: String,
        profileUri: Uri?
    ) : Flow<Resource<String>>

    suspend fun uploadProfileImage(uri: Uri?) : Flow<Resource<String>>
}
