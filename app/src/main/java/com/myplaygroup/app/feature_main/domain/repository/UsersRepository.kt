package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.AppUser
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    suspend fun getAllUsers(
        fetchFromRemote: Boolean
    ) : Flow<Resource<List<AppUser>>>

    suspend fun addUserToDatabase(
        username: String
    ) : Resource<AppUser>

    suspend fun registerUsers(
        users: List<AppUser>
    ): Flow<Resource<List<AppUser>>>
}