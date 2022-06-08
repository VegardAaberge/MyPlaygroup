package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.AppUser
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun getAllUsers(
        fetchFromRemote: Boolean
    ) : Flow<Resource<List<AppUser>>>
}