package com.myplaygroup.app.feature_main.data.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.repository.ChatSocketRepository
import io.ktor.client.*
import javax.inject.Inject

class ChatSocketRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient
) : ChatSocketRepository {
    override suspend fun sendMessage(message: String, receivers: List<String>): Resource<String> {
        return Resource.Error("Not implemented")
    }
}