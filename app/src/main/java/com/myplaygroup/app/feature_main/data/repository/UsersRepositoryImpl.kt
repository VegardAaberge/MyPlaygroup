package com.myplaygroup.app.feature_main.data.repository

import android.content.Context
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.mapper.toAppUser
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.fetchNetworkResource
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.data.model.AppUserEntity
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val mainDatabase: MainDatabase,
    private val tokenRepository: TokenRepository,
    private val api: PlaygroupApi,
    @ApplicationContext private val context: Context
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
                dao.clearSyncedAppUsers()
                dao.insertAppUsers(appUsers)
                dao.getAppUsers().map { it.toAppUser() }
            },
            shouldFetch = {
                fetchFromRemote && checkForInternetConnection(context)
            },
            onFetchError = { r ->
                when(r.code){
                    403 -> tokenRepository.verifyRefreshTokenAndReturnMessage()
                    else -> context.getString(R.string.error_could_not_reach_server, r.message)
                }
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> context.getString(R.string.error_no_internet_connection)
                    else -> {
                        t.localizedMessage?.let {
                            context.getString(R.string.error_server_exception, t.localizedMessage)
                        } ?: context.getString(R.string.error_unknown_error)
                    }
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
            Resource.Error(t.localizedMessage ?: context.getString(R.string.error_unknown_error))
        }
    }

    override suspend fun registerUsers(users: List<AppUser>): Flow<Resource<List<AppUser>>> {
        return fetchNetworkResource(
            fetch = {
                val entitiesToUpload = users.map { x -> x.clientId }
                val userEntities = dao.getAppUsersByClientId(entitiesToUpload)
                api.uploadAppUsers(userEntities)
            },
            processFetch = { appUsers ->
                dao.clearAllAppUsers()
                dao.insertAppUsers(appUsers)
                dao.getAppUsers().map { it.toAppUser() }
            },
            onFetchError = { r ->
                when(r.code){
                    403 -> tokenRepository.verifyRefreshTokenAndReturnMessage()
                    else -> context.getString(R.string.error_could_not_reach_server, r.message)
                }
            },
            onFetchException = { t ->
                when(t){
                    is IOException -> context.getString(R.string.error_no_internet_connection)
                    else -> {
                        t.localizedMessage?.let {
                            context.getString(R.string.error_server_exception, t.localizedMessage)
                        } ?: context.getString(R.string.error_unknown_error)
                    }
                }
            }
        )
    }
}