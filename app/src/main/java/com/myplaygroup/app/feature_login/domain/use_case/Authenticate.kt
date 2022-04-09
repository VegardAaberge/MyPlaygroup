package com.myplaygroup.app.feature_login.domain.use_case

import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.util.Resource

class Authenticate(
    private val repository : LoginRepository
) {
    suspend operator fun invoke(user: String, password: String) : Resource<String> {
        return repository.authenticate(user, password)
    }
}
