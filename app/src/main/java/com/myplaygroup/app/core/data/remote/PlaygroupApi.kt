package com.myplaygroup.app.core.data.remote

import com.myplaygroup.app.feature_login.data.responses.LoginResponse
import com.myplaygroup.app.feature_login.data.responses.RefreshTokenResponse
import com.myplaygroup.app.feature_main.data.model.*
import com.myplaygroup.app.feature_main.data.remote.request.SendMessageRequest
import com.myplaygroup.app.feature_main.data.remote.response.MessagesResponse
import com.myplaygroup.app.feature_main.data.remote.response.ScheduleResponse
import com.myplaygroup.app.feature_main.data.remote.response.SimpleResponse
import com.myplaygroup.app.feature_profile.data.requests.ProfileRequest
import com.myplaygroup.app.feature_profile.data.responses.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface PlaygroupApi {

    @FormUrlEncoded
    @POST("/api/v1/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>

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
    suspend fun getMessages(): Response<MessagesResponse>

    @POST("/api/v1/chat")
    suspend fun sendMessage(
        @Body sendMessageRequest: SendMessageRequest
    ): Response<MessageEntity>

    @GET("api/v1/login/refresh_token")
    suspend fun refreshToken(): Response<RefreshTokenResponse>

    @GET("api/v1/classes")
    suspend fun getAllClasses(): Response<List<DailyClassEntity>>

    @POST("api/v1/classes")
    suspend fun createDailyClasses(
        @Body unsyncedClasses: List<DailyClassEntity>
    ): Response<List<DailyClassEntity>>

    @GET("api/v1/schedule")
    suspend fun getSchedule(): Response<ScheduleResponse>

    @GET("api/v1/plans")
    suspend fun getMonthlyPlans() : Response<List<MonthlyPlanEntity>>

    @GET("api/v1/plans/standard")
    suspend fun getStandardPlans() : Response<List<StandardPlanEntity>>

    @POST("api/v1/plans")
    suspend fun uploadMonthlyPlans(
        @Body monthlyPlanEntities: List<MonthlyPlanEntity>
    ): Response<List<MonthlyPlanEntity>>

    @GET("api/v1/users")
    suspend fun getAppUsers() : Response<List<AppUserEntity>>

    @POST("api/v1/users")
    suspend fun uploadAppUsers(
        @Body unsyncedClasses: List<AppUserEntity>
    ): Response<List<AppUserEntity>>
}