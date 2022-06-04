package com.myplaygroup.app.feature_login.domain.repository

import android.net.Uri
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_login.data.responses.LoginResponse
import com.myplaygroup.app.feature_login.data.responses.SendResetPasswordResponse
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun authenticate(username: String, password: String) : Flow<Resource<LoginResponse>>

    suspend fun sendEmailRequestForm(email: String): Flow<Resource<SendResetPasswordResponse>>

    suspend fun checkVerificationCode(code: String, token: String): Flow<Resource<Unit>>
}
