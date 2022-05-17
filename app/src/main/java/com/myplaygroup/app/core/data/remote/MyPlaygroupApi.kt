package com.myplaygroup.app.core.data.remote

import com.myplaygroup.app.feature_login.data.remote.requests.*
import com.myplaygroup.app.feature_login.data.remote.responses.LoginResponse
import com.myplaygroup.app.feature_login.data.remote.responses.RefreshTokenResponse
import com.myplaygroup.app.feature_login.data.remote.responses.SimpleResponse
import com.myplaygroup.app.feature_main.data.models.MessageEntity
import com.myplaygroup.app.feature_main.data.remote.MessageRequest
import com.myplaygroup.app.feature_main.data.remote.MessageResponse
import com.myplaygroup.app.feature_profile.data.requests.ProfileRequest
import com.myplaygroup.app.feature_profile.data.responses.ProfileResponse
import retrofit2.Response
import retrofit2.http.*
import java.util.*

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

    @POST("/api/v1/registration/profile/create/{username}")
    suspend fun createProfile(
        @Path("username") username: String,
        @Body codeRequest: ProfileRequest
    ): Response<ProfileResponse>

    @POST("/api/v1/registration/profile/edit/{username}")
    suspend fun editProfile(
        @Path("username") username: String,
        @Body codeRequest: ProfileRequest
    ): Response<ProfileResponse>

    @GET("/api/v1/chat")
    suspend fun getMessages(): MessageResponse

    @POST("/api/v1/chat")
    suspend fun sendMessage(
        @Body messageRequest: MessageRequest
    ): Response<MessageEntity>

    @GET("api/v1/login/refresh_token/")
    suspend fun refreshToken(): Response<RefreshTokenResponse>


}