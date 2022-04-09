package com.myplaygroup.app.feature_login.data.repository

import com.myplaygroup.app.feature_login.domain.repository.LoginRepository

class LoginRepositoryImpl : LoginRepository {
    override suspend fun authenticate(user: String, password: String) {

    }
}
