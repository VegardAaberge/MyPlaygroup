package com.myplaygroup.app.feature_main.data.repository

import android.app.Application
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.core.data.mapper.toDailyClass
import com.myplaygroup.app.core.data.mapper.toDailyClassEntity
import com.myplaygroup.app.core.util.fetchNetworkResource
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.domain.repository.DailyClassesRepository
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class DailyClassesRepositoryImpl @Inject constructor(
    private val mainDatabase: MainDatabase,
    private val tokenRepository: TokenRepository,
    private val api: PlaygroupApi,
    private val app: Application
) : DailyClassesRepository {

    val dao = mainDatabase.mainDao()

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
            saveFetchResult = { dailyClasses ->
                dao.clearDailyClasses()
                dao.insertDailyClasses(dailyClasses)
                dao.getDailyClasses().map { it.toDailyClass() }
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

    override fun uploadDailyClasses(
        unsyncedClasses: List<DailyClass>
    ): Flow<Resource<List<DailyClass>>> {
        return fetchNetworkResource(
            fetch = {
                val entitiesToUpload = unsyncedClasses.map { x -> x.toDailyClassEntity() }
                api.createDailyClasses(entitiesToUpload)
            },
            processFetch = { dailyClasses ->
                dao.clearDailyClasses()
                dao.insertDailyClasses(dailyClasses)
                dao.getDailyClasses().map { it.toDailyClass() }
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