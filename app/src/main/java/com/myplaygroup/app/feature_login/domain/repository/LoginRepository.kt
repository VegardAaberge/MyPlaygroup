package com.myplaygroup.app.feature_login.domain.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_login.data.responses.LoginResponse
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun authenticate(username: String, password: String) : Flow<Resource<LoginResponse>>
}
