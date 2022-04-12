package com.myplaygroup.app.feature_login.data.repository

import com.myplaygroup.app.feature_login.domain.model.User
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.core.util.Resource
import kotlinx.coroutines.delay
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor() : LoginRepository {

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

    override suspend fun sendEmailRequestForm(email: String): Resource<String> {
        delay(3000)

        return  Resource.Success("Sent")
    }

    override suspend fun checkVerificationCode(code: String): Resource<String> {
        delay(3000)

        if(code == "12345"){
            return Resource.Success<String>("")
        }else{
            return Resource.Error(message = "Fail")
        }
    }
}


