package com.myplaygroup.app.feature_main.data.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUsersRepository : UsersRepository {

    var appUsers = mutableListOf<AppUser>(
        AppUser(
            id = 1,
            locked = true,
            phoneNumber = "null",
            profileName = "null",
            profileCreated = false,
            balance = 500,
            username = "meng"
        ),
        AppUser(
            id = 2,
            locked = false,
            phoneNumber = "12345678901",
            profileName = "Vegard",
            profileCreated = true,
            balance = 200,
            username = "vegard"
        )
    )

    override suspend fun getAllUsers(fetchFromRemote: Boolean): Flow<Resource<List<AppUser>>> {
        return flow {
            emit(Resource.Success(appUsers))
        }
    }

    override suspend fun addUserToDatabase(username: String): Resource<AppUser> {
        TODO("Not yet implemented")
    }

    override suspend fun registerUsers(users: List<AppUser>): Flow<Resource<List<AppUser>>> {
        TODO("Not yet implemented")
    }
}