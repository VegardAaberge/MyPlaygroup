package com.myplaygroup.app.feature_login.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.network.fetchNetworkResource
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.util.Constants.DEBUG_KEY
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.feature_login.data.requests.SendEmailRequest
import com.myplaygroup.app.feature_login.data.requests.VerifyCodeRequest
import com.myplaygroup.app.feature_login.data.responses.LoginResponse
import com.myplaygroup.app.feature_login.data.responses.SendResetPasswordResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context
) : LoginRepository {

    override suspend fun authenticate(
        username: String,
        password: String,
    ) : Flow<Resource<LoginResponse>> {

        return fetchNetworkResource(
            fetch = {
                api.login(
                    username = username,
                    password = password
                )
            },
            processFetch = { loginResponse ->
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
                loginResponse
            },
            onFetchError = { r ->
                when(r.code()){
                    403 -> "Wrong username or password"
                    else -> "Couldn't reach server: ${r.message()}"
                }
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> "No Internet Connection"
                    else -> "Server Exception: " + (t.localizedMessage ?: "Unknown exception")
                }
            }
        )
    }

    override suspend fun sendEmailRequestForm(
        email: String
    ): Flow<Resource<SendResetPasswordResponse>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                val response = api.sendEmailRequest(
                    SendEmailRequest(email)
                )

                if(response.isSuccessful && response.code() == 200 && response.body() != null){
                    val body = response.body()!!
                    emit(Resource.Success(body))
                }else{
                    val errorMessage = "Code: ${response.code()} Error: ${response.message()}"
                    Log.e(DEBUG_KEY, errorMessage)
                    emit(Resource.Error(errorMessage))
                }

            }catch (e: Exception){
                Log.e(DEBUG_KEY, e.stackTraceToString())
                emit(Resource.Error(context.getString(R.string.api_login_exception)))
            }finally {
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun checkVerificationCode(
        code: String,
        token: String
    ): Flow<Resource<String>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                val response = api.checkVerificationCode(
                    VerifyCodeRequest(
                        token = token,
                        code = code
                    )
                )

                if(response.isSuccessful && response.code() == 200 && response.body() != null){
                    val body = response.body()!!
                    emit(Resource.Success(body.message))
                }else{
                    emit(Resource.Error("Error: " + response.message()))
                }

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
}


