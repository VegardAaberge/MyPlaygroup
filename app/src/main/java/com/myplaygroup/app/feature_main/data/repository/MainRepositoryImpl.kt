package com.myplaygroup.app.feature_main.data.repository

import com.bumptech.glide.load.engine.Resource
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(

) : MainRepository {

    override suspend fun getChatMessages(): Flow<Resource<List<Message>>> {
        TODO("Not yet implemented")
    }
}