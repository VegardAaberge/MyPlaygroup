package com.myplaygroup.app.feature_admin.data.repository

import android.app.Application
import android.content.Context
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_admin.data.local.AdminDatabase
import com.myplaygroup.app.feature_admin.data.local.DailyClassEntity
import com.myplaygroup.app.feature_admin.data.mapper.toDailyClass
import com.myplaygroup.app.feature_admin.data.mapper.toDailyClassEntity
import com.myplaygroup.app.feature_admin.domain.model.DailyClass
import com.myplaygroup.app.feature_admin.domain.repository.DailyClassesRepository
import com.myplaygroup.app.feature_main.data.mapper.toMessageEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class DailyClassesRepositoryImpl @Inject constructor(
    private val adminDatabase: AdminDatabase,
    private val tokenRepository: TokenRepository,
    private val api: PlaygroupApi,
    private val app: Application
) : DailyClassesRepository {

    val dao = adminDatabase.mainDao()

    override fun getAllDailyClasses(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<DailyClass>>> {
        return networkBoundResource(
            query = {
                dao.getDailyClasses().map { x -> x.toDailyClass() }
            },
            fetch = {
                api.getAllClasses()
            },
            saveFetchResult = { dailyClassResponse ->
                val dailyClasses = dailyClassResponse.map { it.toDailyClassEntity() }
                dao.clearDailyClasses()
                dao.insertDailyClasses(dailyClasses)
                val classEntities = dao.getDailyClasses()
                classEntities.map { it.toDailyClass() }
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

    override fun createDailyClasses(): Flow<Resource<Unit>> {
        TODO("Not yet implemented")
    }
}