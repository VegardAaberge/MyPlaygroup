package com.myplaygroup.app.feature_login.data.repository

import android.content.Context
import android.util.Log
import com.myplaygroup.app.R
import com.myplaygroup.app.core.util.Constants.DEBUG_KEY
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.data.remote.MyPlaygroupApi
import com.myplaygroup.app.feature_login.data.remote.requests.LoginRequest
import com.myplaygroup.app.feature_login.data.remote.requests.SendEmailRequest
import com.myplaygroup.app.feature_login.data.remote.requests.VerifyCodeRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import no.vegardaaberge.data.responses.SimpleResponse
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: MyPlaygroupApi,
    @ApplicationContext private val context: Context
) : LoginRepository {

    override suspend fun authenticate(
        username: String,
        password: String
    ) : Flow<Resource<String>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                val response = api.login(
                    LoginRequest(username, password)
                )

                emit(getSuccessResponse(response))

            }catch (e: Exception){
                Log.e(DEBUG_KEY, e.stackTraceToString())
                emit(Resource.Error(context.getString(R.string.api_login_exception)))
            }finally {
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun sendEmailRequestForm(
        email: String
    ): Flow<Resource<String>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                val response = api.sendEmailRequest(
                    SendEmailRequest(email)
                )

                emit(getSuccessResponse(response))

            }catch (e: Exception){
                Log.e(DEBUG_KEY, e.stackTraceToString())
                emit(Resource.Error(context.getString(R.string.api_login_exception)))
            }finally {
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun checkVerificationCode(
        code: String
    ): Flow<Resource<String>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                val response = api.checkVerificationCode(
                    VerifyCodeRequest(code)
                )

                emit(getSuccessResponse(response))

            }catch (e: Exception){
                Log.e(DEBUG_KEY, e.stackTraceToString())
                emit(Resource.Error(context.getString(R.string.api_login_exception)))
            }finally {
                emit(Resource.Loading(false))
            }
        }
    }

    private fun getSuccessResponse(response: Response<SimpleResponse>) : Resource<String> {

        val responseMessage = response.body()?.message
        return if(response.isSuccessful && response.body()!!.successful){
            Resource.Success(responseMessage)
        }else{
            Resource.Error(responseMessage ?: response.message())
        }
    }
}


