package com.myplaygroup.app.feature_login.data.repository

import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.util.Resource
import kotlinx.coroutines.delay

class LoginRepositoryImpl : LoginRepository {

    override suspend fun authenticate(user: String, password: String) : Resource<String> {
        delay(3000)

        if(user == "vegard" && password == "123"){
            return Resource.Success<String>("Success")
        }else{
            return Resource.Error(data = "Fail", message = "Fail")
        }
    }
}


