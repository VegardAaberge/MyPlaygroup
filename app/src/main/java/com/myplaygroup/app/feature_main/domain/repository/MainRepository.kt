package com.myplaygroup.app.feature_main.domain.repository

import com.bumptech.glide.load.engine.Resource
import com.myplaygroup.app.feature_main.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getChatMessages() : Flow<Resource<List<Message>>>
}