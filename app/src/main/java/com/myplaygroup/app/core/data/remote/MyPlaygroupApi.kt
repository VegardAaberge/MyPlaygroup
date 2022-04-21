package com.myplaygroup.app.core.data.remote

import com.myplaygroup.app.feature_login.data.remote.requests.*
import com.myplaygroup.app.feature_login.data.remote.responses.LoginResponse
import com.myplaygroup.app.feature_login.data.remote.responses.SimpleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MyPlaygroupApi {

    @POST("/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<SimpleResponse>

    @POST("/sendEmailRequest")
    suspend fun sendEmailRequest(
        @Body emailRequest: SendEmailRequest
    ): Response<SimpleResponse>

    @POST("/checkVerificationCode")
    suspend fun checkVerificationCode(
        @Body codeRequest: VerifyCodeRequest
    ): Response<SimpleResponse>

    @POST("/registerProfile")
    suspend fun registerProfile(
        @Body codeRequest: ProfileRequest
    ): Response<SimpleResponse>
}