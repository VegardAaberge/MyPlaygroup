package com.myplaygroup.app.feature_main.data.repository

import android.app.Application
import com.myplaygroup.app.core.data.mapper.toAppUser
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.*
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.data.model.AppUserEntity
import com.myplaygroup.app.feature_main.data.remote.request.RegistrationRequest
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val mainDatabase: MainDatabase,
    private val tokenRepository: TokenRepository,
    private val api: PlaygroupApi,
    private val app: Application
) : UsersRepository {

    val dao = mainDatabase.mainDao()

    override suspend fun getAllUsers(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<AppUser>>> {

        return networkBoundResource(
            query = {
                dao.getAppUsers().map { x -> x.toAppUser() }
            },
            fetch = {
                api.getAppUsers()
            },
            saveFetchResult = { appUsers ->
                dao.clearAppUsers()
                dao.insertAppUsers(appUsers)
                dao.getAppUsers().map { it.toAppUser() }
            },
            shouldFetch = {
                fetchFromRemote && checkForInternetConnection(app)
            },
            onFetchError = { r ->
                when(r.code){
                    403 -> tokenRepository.verifyRefreshTokenAndReturnMessage()
                    else -> "Couldn't reach server: ${r.message}"
                }
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> "No Internet Connection"
                    else -> "Server Exception: " + (t.localizedMessage ?: "Unknown exception")
                }
            }
        )
    }

    override suspend fun addUserToDatabase(username: String): Resource<AppUser> {
        return try {
            val appUser = AppUserEntity(
                username = username
            )
            dao.insertAppUser(appUser)
            Resource.Success(appUser.toAppUser())
        } catch (t: Throwable) {
            t.printStackTrace()
            Resource.Error(t.localizedMessage ?: "Unknown error")
        }
    }

    override fun unsyncedUsers(unsyncedClasses: List<AppUser>): Flow<Resource<List<AppUser>>> {
        return fetchNetworkResource(
            fetch = {
                val entitiesToUpload = unsyncedClasses.map { x -> x.clientId }
                val userEntities = dao.getAppUsersById(entitiesToUpload)
                val registerRequests = userEntities.map { x -> RegistrationRequest(
                    username = x.username,
                    password = Constants.MY_PLAYGROUP
                )}
                api.createAppUsers(registerRequests)
            },
            processFetch = { appUsers ->
                dao.clearAllAppUsers()
                dao.insertAppUsers(appUsers)
                dao.getAppUsers().map { it.toAppUser() }
            },
            onFetchError = { r ->
                when(r.code){
                    403 -> tokenRepository.verifyRefreshTokenAndReturnMessage()
                    else -> "Couldn't reach server: ${r.message}"
                }
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> "No Internet Connection"
                    else -> "Server Exception: " + (t.localizedMessage ?: "Unknown exception")
                }
            }
        )
    }
}