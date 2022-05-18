package com.myplaygroup.app.core.data.remote

import com.myplaygroup.app.feature_login.data.requests.*
import com.myplaygroup.app.feature_login.data.responses.LoginResponse
import com.myplaygroup.app.feature_login.data.responses.RefreshTokenResponse
import com.myplaygroup.app.feature_login.data.responses.SendResetPasswordResponse
import com.myplaygroup.app.feature_main.data.models.MessageEntity
import com.myplaygroup.app.feature_main.data.requests.MessageRequest
import com.myplaygroup.app.feature_main.data.requests.MessageResponse
import com.myplaygroup.app.feature_profile.data.requests.ProfileRequest
import com.myplaygroup.app.feature_profile.data.responses.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.io.InputStream

interface PlaygroupApi {

    @FormUrlEncoded
    @POST("/api/v1/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @POST("/api/v1/reset-password/send")
    suspend fun sendEmailRequest(
        @Body emailRequest: SendEmailRequest
    ): Response<SendResetPasswordResponse>

    @POST("/api/v1/reset-password/verify")
    suspend fun checkVerificationCode(
        @Body codeRequest: VerifyCodeRequest
    ): Response<SimpleResponse>

    @POST("/api/v1/profile/create/{username}")
    suspend fun createProfile(
        @Path("username") username: String,
        @Body codeRequest: ProfileRequest
    ): Response<ProfileResponse>

    @POST("/api/v1/profile/edit/{username}")
    suspend fun editProfile(
        @Path("username") username: String,
        @Body codeRequest: ProfileRequest
    ): Response<ProfileResponse>

    @GET("/api/v1/profile/get-image/{username}")
    suspend fun getProfileImage(
        @Path("username") username: String,
    ): ResponseBody

    @Multipart
    @POST("/api/v1/profile/upload-image/")
    suspend fun uploadProfileImage(
        @Part image: MultipartBody.Part,
    ): Response<SimpleResponse>

    @GET("/api/v1/chat")
    suspend fun getMessages(): MessageResponse

    @POST("/api/v1/chat")
    suspend fun sendMessage(
        @Body messageRequest: MessageRequest
    ): Response<MessageEntity>

    @GET("api/v1/login/refresh_token")
    suspend fun refreshToken(): Response<RefreshTokenResponse>


}