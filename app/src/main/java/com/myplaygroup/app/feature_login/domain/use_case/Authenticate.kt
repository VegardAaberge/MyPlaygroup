package com.myplaygroup.app.feature_login.domain.use_case

import com.myplaygroup.app.feature_login.domain.repository.LoginRepository

class Authenticate(
    private val repository : LoginRepository
) {
    suspend operator fun invoke(user: String, password: String){
        return repository.authenticate(user, password)
    }
}
