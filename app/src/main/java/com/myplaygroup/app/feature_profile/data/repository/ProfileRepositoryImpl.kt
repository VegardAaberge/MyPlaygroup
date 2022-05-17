package com.myplaygroup.app.feature_profile.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.data.remote.MyPlaygroupApi
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.KEY_EMAIL
import com.myplaygroup.app.core.util.Constants.KEY_PHONE_NUMBER
import com.myplaygroup.app.core.util.Constants.KEY_PROFILE_NAME
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_profile.data.requests.ProfileRequest
import com.myplaygroup.app.feature_login.data.remote.responses.SimpleResponse
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.lang.Exception
import java.lang.IllegalStateException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: MyPlaygroupApi,
    private val sharedPreferences: SharedPreferences,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    @ApplicationContext private val context: Context
) : ProfileRepository {

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
                val username = sharedPreferences.getString(Constants.KEY_USERNAME, Constants.NO_VALUE) ?: Constants.NO_VALUE

                var response = api.createProfile(
                    username,
                    ProfileRequest(
                        profileName = profileName,
                        phoneNumber = phoneNumber,
                        email = email,
                        password = newPassword
                    )
                )

                if(response.code() == 403 && verifyRefreshToken()){
                    response = api.editProfile(
                        username = username,
                        ProfileRequest(
                            profileName = profileName,
                            phoneNumber = phoneNumber,
                            email = email
                        )
                    )
                }

                if(response.isSuccessful && response.code() == 200 && response.body() != null){
                    val body = response.body()!!
                    sharedPreferences.edit {
                        putString(KEY_PROFILE_NAME, body.profileName)
                        putString(KEY_PHONE_NUMBER, body.phoneNumber)
                        putString(KEY_EMAIL, body.email)
                        apply()
                    }
                    emit(Resource.Success("SUCCESS"))
                }else {
                    emit(Resource.Error("ERROR: ${response.message()}"))
                }
            }
            catch (e : IllegalStateException){
                Log.e(Constants.DEBUG_KEY, e.stackTraceToString())
                emit(Resource.Error(e.message.toString()))
            }
            catch (e: Exception){
                Log.e(Constants.DEBUG_KEY, e.stackTraceToString())
                emit(Resource.Error(context.getString(R.string.api_login_exception)))
            }finally {
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun editProfile(
        profileName: String,
        phoneNumber: String,
        email: String
    ): Flow<Resource<String>> {

        return flow {
            emit(Resource.Loading(true))

            try {
                val username = sharedPreferences.getString(Constants.KEY_USERNAME, Constants.NO_VALUE) ?: Constants.NO_VALUE

                var response = api.editProfile(
                    username = username,
                    ProfileRequest(
                        profileName = profileName,
                        phoneNumber = phoneNumber,
                        email = email
                    )
                )

                if(response.code() == 403 && verifyRefreshToken()){
                    response = api.editProfile(
                        username = username,
                        ProfileRequest(
                            profileName = profileName,
                            phoneNumber = phoneNumber,
                            email = email
                        )
                    )
                }

                if(response.isSuccessful && response.code() == 200 && response.body() != null){
                    val body = response.body()!!
                    sharedPreferences.edit {
                        putString(KEY_PROFILE_NAME, body.profileName)
                        putString(KEY_PHONE_NUMBER, body.phoneNumber)
                        putString(KEY_EMAIL, body.email)
                        apply()
                    }
                    emit(Resource.Success("SUCCESS"))
                }else {
                    emit(Resource.Error("ERROR: ${response.message()}"))
                }
            }
            catch (e: Exception){
                Log.e(Constants.DEBUG_KEY, e.stackTraceToString())
                emit(Resource.Error(context.getString(R.string.api_login_exception)))
            }finally {
                emit(Resource.Loading(false))
            }
        }
    }

    suspend fun verifyRefreshToken() : Boolean {
        if(basicAuthInterceptor.accessToken != null)
            return false

        val refreshToken = sharedPreferences.getString(
            Constants.KEY_REFRESH_TOKEN,
            Constants.NO_VALUE
        ) ?: Constants.NO_VALUE;
        if(refreshToken == Constants.NO_VALUE)
            return false

        basicAuthInterceptor.accessToken = refreshToken

        val response = api.refreshToken()

        if(response.isSuccessful && response.body() != null){
            val body = response.body()!!

            basicAuthInterceptor.accessToken = body.access_token
            sharedPreferences.edit {
                putString(Constants.KEY_ACCESS_TOKEN, body.access_token)
                putString(Constants.KEY_REFRESH_TOKEN, body.refresh_token)
            }
        }else{
            basicAuthInterceptor.accessToken = null
            sharedPreferences.edit {
                putString(Constants.KEY_ACCESS_TOKEN, Constants.NO_VALUE)
                putString(Constants.KEY_REFRESH_TOKEN, Constants.NO_VALUE)
            }
        }

        return response.isSuccessful && response.body() != null
    }
}