package com.myplaygroup.app.core.data.remote

import no.vegardaaberge.data.requests.AccountRequest
import no.vegardaaberge.data.responses.SimpleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MyPlaygroupApi {

    @POST("/login")
    suspend fun login(
        @Body loginRequest: AccountRequest
    ): Response<SimpleResponse>

    @POST("/register")
    suspend fun register(
        @Body registerRequest: AccountRequest
    ): Response<SimpleResponse>
}