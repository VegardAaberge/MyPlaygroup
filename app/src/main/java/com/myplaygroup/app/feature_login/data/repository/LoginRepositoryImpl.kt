package com.myplaygroup.app.feature_login.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.util.Constants.DEBUG_KEY
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.data.remote.MyPlaygroupApi
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.feature_login.data.remote.requests.LoginRequest
import com.myplaygroup.app.feature_login.data.remote.requests.ProfileRequest
import com.myplaygroup.app.feature_login.data.remote.requests.SendEmailRequest
import com.myplaygroup.app.feature_login.data.remote.requests.VerifyCodeRequest
import com.myplaygroup.app.feature_login.data.remote.responses.LoginResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import no.vegardaaberge.data.responses.SimpleResponse
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: MyPlaygroupApi,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context
) : LoginRepository {

    override suspend fun authenticate(
        username: String,
        password: String,
    ) : Flow<Resource<LoginResponse>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                val response = api.login(
                    LoginRequest(username, password)
                )

                val responseMessage = response.body()?.message
                if(response.isSuccessful && response.body()!!.successful){
                    basicAuthInterceptor.username = username
                    basicAuthInterceptor.password = password

                    if(!response.body()!!.createProfile)
                    {
                        sharedPreferences.edit {
                            putString(Constants.KEY_USERNAME, username)
                            putString(Constants.KEY_PASSWORD, password)
                            apply()
                        }
                    }

                    emit(
                        Resource.Success(response.body())
                    )
                }else{
                    emit(
                        Resource.Error(responseMessage ?: response.message())
                    )
                }

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

    override suspend fun createProfile(
        profileName: String,
        phoneNumber: String,
        email: String,
        newPassword: String,
        profileUri: Uri?
    ): Flow<Resource<String>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                val response = api.registerProfile(
                    ProfileRequest(
                        profileName = profileName,
                        phoneNumber = phoneNumber,
                        email = email,
                        newPassword = newPassword
                    )
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

    override suspend fun uploadProfileImage(uri: Uri?) : Flow<Resource<String>> {

        return flow {
            try {
                emit(Resource.Loading(true))
                // TODO upload image
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


