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
import com.myplaygroup.app.core.util.Constants.NO_VALUE
import com.myplaygroup.app.feature_login.data.remote.requests.ProfileRequest
import com.myplaygroup.app.feature_login.data.remote.requests.SendEmailRequest
import com.myplaygroup.app.feature_login.data.remote.requests.VerifyCodeRequest
import com.myplaygroup.app.feature_login.data.remote.responses.LoginResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.myplaygroup.app.feature_login.data.remote.responses.SimpleResponse
import retrofit2.Response
import java.lang.Exception
import java.lang.IllegalStateException
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
                    username = username,
                    password = password
                )

                if(response.isSuccessful){
                    if(response.body() != null){
                        val loginResponse = response.body()!!;
                        basicAuthInterceptor.accessToken = loginResponse.access_token
                        sharedPreferences.edit() {
                            putString(Constants.KEY_USERNAME, username)
                            putString(Constants.KEY_ACCESS_TOKEN, loginResponse.access_token)
                            putString(Constants.KEY_REFRESH_TOKEN, loginResponse.refresh_token)

                            if(loginResponse.profile_created){
                                putString(Constants.KEY_PROFILE_NAME, loginResponse.profile_name)
                                putString(Constants.KEY_EMAIL, loginResponse.email)
                                putString(Constants.KEY_PHONE_NUMBER, loginResponse.phone_number)
                            }
                            apply()
                        }
                        emit(
                            Resource.Success(loginResponse)
                        )

                    }else{
                        emit(
                            Resource.Error("Response body is missing")
                        )
                    }
                }else if(response.code() == 401) {
                    emit(Resource.Error("??? Error: ${response.message()}"))
                }else if(response.code() == 402) {
                    emit(Resource.Error("??? Error: ${response.message()}"))
                }else if(response.code() == 403) {
                    emit(Resource.Error("Wrong username or password"))
                }else if(response.code() == 404) {
                    emit(Resource.Error("Not Found Exception: ${response.message()}"))
                }else if(response.code() == 500) {
                    emit(Resource.Error("Server error: ${response.message()}"))
                }else{
                    emit(Resource.Error("Unknown error: ${response.message()}"))
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
                val username = sharedPreferences.getString(Constants.KEY_USERNAME, NO_VALUE) ?: NO_VALUE
                if(username == NO_VALUE){
                    throw IllegalStateException("No username found")
                }

                val response = api.registerProfile(
                    username,
                    ProfileRequest(
                        profileName = profileName,
                        phoneNumber = phoneNumber,
                        email = email,
                        password = newPassword
                    )
                )

                emit(getSuccessResponse(response))

            }
            catch (e : IllegalStateException){
                Log.e(DEBUG_KEY, e.stackTraceToString())
                emit(Resource.Error(e.message.toString()))
            }
            catch (e: Exception){
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


