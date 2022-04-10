package com.myplaygroup.app.feature_login.data.repository

import com.myplaygroup.app.feature_login.domain.model.User
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.core.util.Resource
import kotlinx.coroutines.delay

class LoginRepositoryImpl : LoginRepository {

    override suspend fun authenticate(user: String, password: String) : Resource<User> {
        delay(3000)

        if(user == "vegard" && password == "123"){
            return Resource.Success<User>(User(
                id = 1,
                token = "12345"
            ))
        }else{
            return Resource.Error(message = "Fail")
        }
    }
}


