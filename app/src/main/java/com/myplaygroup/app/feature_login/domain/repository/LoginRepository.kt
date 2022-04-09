package com.myplaygroup.app.feature_login.domain.repository

interface LoginRepository {
    suspend fun authenticate(user: String, password: String)
}
