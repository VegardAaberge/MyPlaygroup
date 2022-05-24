package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Resource

interface ChatSocketRepository {

    suspend fun sendMessage(
        message: String,
        receivers: List<String>
    ): Resource<String>
}