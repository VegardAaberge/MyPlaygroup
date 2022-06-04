package com.myplaygroup.app.feature_profile.data.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import com.myplaygroup.app.core.data.model.AppUser
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeProfileRepository() : ProfileRepository {

    val users = mutableListOf(
        AppUser(username = "vegard"),
        AppUser(username = "meng"),
        AppUser(username = "admin")
    )

    override suspend fun createProfile(
        username: String,
        profileName: String,
        phoneNumber: String,
        email: String,
        newPassword: String
    ): Flow<Resource<Unit>> {

        val user = users.first { x -> x.username == username }

        val newUser = user.copy(
            profileName = profileName,
            phoneNumber = phoneNumber,
            email = email,
            password = newPassword
        )

        users.remove(user)
        users.add(newUser)

        return flow { emit(Resource.Success())  }
    }

    override suspend fun editProfile(
        username: String,
        profileName: String,
        phoneNumber: String,
        email: String
    ): Flow<Resource<Unit>> {

        val user = users.first { x -> x.username == username }

        val newUser = user.copy(
            profileName = profileName,
            phoneNumber = phoneNumber,
            email = email,
        )

        users.remove(user)
        users.add(newUser)

        return flow { emit(Resource.Success())  }
    }
}