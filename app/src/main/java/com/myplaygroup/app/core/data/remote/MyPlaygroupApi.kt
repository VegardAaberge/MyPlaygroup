package com.myplaygroup.app.core.data.remote

import com.myplaygroup.app.feature_login.data.remote.requests.*
import com.myplaygroup.app.feature_login.data.remote.responses.LoginResponse
import com.myplaygroup.app.feature_login.data.remote.responses.RefreshTokenResponse
import com.myplaygroup.app.feature_login.data.remote.responses.SimpleResponse
import com.myplaygroup.app.feature_main.data.remote.MessageResponse
import retrofit2.Response
import retrofit2.http.*

interface MyPlaygroupApi {

    @FormUrlEncoded
    @POST("/api/v1/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
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

    @POST("/api/v1/registration/update/{username}")
    suspend fun registerProfile(
        @Path("username") username: String,
        @Body codeRequest: ProfileRequest
    ): Response<SimpleResponse>

    @GET("/api/v1/chat")
    suspend fun getMessages(): MessageResponse

    @GET("api/v1/login/refresh_token/")
    suspend fun refreshToken(): Response<RefreshTokenResponse>
}