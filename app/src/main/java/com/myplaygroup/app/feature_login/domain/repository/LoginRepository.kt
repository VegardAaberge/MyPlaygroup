package com.myplaygroup.app.feature_login.domain.repository

import com.myplaygroup.app.util.Resource

interface LoginRepository {
    suspend fun authenticate(user: String, password: String) : Resource<String>
}
