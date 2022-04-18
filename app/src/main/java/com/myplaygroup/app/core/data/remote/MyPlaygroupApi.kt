package com.myplaygroup.app.core.data.remote

import com.myplaygroup.app.feature_login.data.remote.requests.LoginRequest
import com.myplaygroup.app.feature_login.data.remote.requests.RegisterRequest
import com.myplaygroup.app.feature_login.data.remote.requests.SendEmailRequest
import com.myplaygroup.app.feature_login.data.remote.requests.VerifyCodeRequest
import no.vegardaaberge.data.responses.SimpleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MyPlaygroupApi {

    @POST("/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<SimpleResponse>

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
}