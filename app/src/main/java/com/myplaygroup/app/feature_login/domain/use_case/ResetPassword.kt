package com.myplaygroup.app.feature_login.domain.use_case

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_login.domain.model.User
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository

class ResetPassword(
    private val repository : LoginRepository
) {
    suspend operator fun invoke(email: String) : Resource<String> {
        return repository.resetPassword(email)
    }
}