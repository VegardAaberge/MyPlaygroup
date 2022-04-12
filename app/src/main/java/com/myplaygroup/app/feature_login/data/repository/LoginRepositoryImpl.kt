package com.myplaygroup.app.feature_login.data.repository

import com.myplaygroup.app.feature_login.domain.model.User
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.core.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor() : LoginRepository {

    override suspend fun authenticate(
        user: String,
        password: String
    ) : Flow<Resource<User>> {

        return flow {
            emit(Resource.Loading(true))
            delay(3000)
            emit(Resource.Loading(false))

            if(user == "vegard" && password == "123"){
                val newUser = User(
                    id = 1,
                    token = "12345"
                )

                emit(Resource.Success(newUser))
            }else{
                emit(Resource.Error(message = "Fail"))
            }
        }
    }

    override suspend fun sendEmailRequestForm(
        email: String
    ): Flow<Resource<String>> {

        return flow {
            emit(Resource.Loading(true))
            delay(3000)
            emit(Resource.Loading(false))

            if(email == "vegard")
                emit(Resource.Success())
            else
                emit(Resource.Error("No such user exist"))
        }
    }

    override suspend fun checkVerificationCode(
        code: String
    ): Flow<Resource<String>> {

        return flow {
            emit(Resource.Loading(true))
            delay(3000)
            emit(Resource.Loading(false))

            if(code == "12345"){
                emit(Resource.Success<String>())
            }else{
                emit(Resource.Error(message = "Fail"))
            }
        }
    }
}


