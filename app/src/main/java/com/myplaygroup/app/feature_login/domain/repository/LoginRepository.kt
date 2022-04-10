package com.myplaygroup.app.feature_login.domain.repository

import com.myplaygroup.app.feature_login.domain.model.User
import com.myplaygroup.app.core.util.Resource

interface LoginRepository {
    suspend fun authenticate(user: String, password: String) : Resource<User>

    suspend fun resetPassword(email: String): Resource<String>
}
