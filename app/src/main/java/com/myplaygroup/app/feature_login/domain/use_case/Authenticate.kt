package com.myplaygroup.app.feature_login.domain.use_case

import com.myplaygroup.app.feature_login.domain.model.User
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.core.util.Resource

class Authenticate(
    private val repository : LoginRepository
) {
    suspend operator fun invoke(user: String, password: String) : Resource<User> {
        return repository.authenticate(user, password)
    }
}
