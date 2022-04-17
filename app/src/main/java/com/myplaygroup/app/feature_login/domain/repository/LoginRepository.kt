package com.myplaygroup.app.feature_login.domain.repository

import com.myplaygroup.app.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun authenticate(user: String, password: String) : Flow<Resource<String>>

    suspend fun sendEmailRequestForm(email: String): Flow<Resource<String>>

    suspend fun checkVerificationCode(code: String): Flow<Resource<String>>
}
