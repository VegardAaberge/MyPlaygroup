package com.myplaygroup.app.feature_main.data.repository

import android.content.Context
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.mapper.toDailyClass
import com.myplaygroup.app.core.data.mapper.toDailyClassEntity
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.fetchNetworkResource
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.repository.DailyClassesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class DailyClassesRepositoryImpl @Inject constructor(
    private val mainDatabase: MainDatabase,
    private val tokenRepository: TokenRepository,
    private val api: PlaygroupApi,
    @ApplicationContext private val context: Context
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
                dao.clearSyncedDailyClasses()
                dao.insertDailyClasses(dailyClasses)
                dao.getDailyClasses().map { it.toDailyClass() }
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
                            context.getString(R.string.error_could_not_reach_server, t.localizedMessage)
                        } ?: context.getString(R.string.error_unknown_error)
                    }
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
                dao.clearAllDailyClasses()
                dao.insertDailyClasses(dailyClasses)
                dao.getDailyClasses().map { it.toDailyClass() }
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
                            context.getString(R.string.error_could_not_reach_server, t.localizedMessage)
                        } ?: context.getString(R.string.error_unknown_error)
                    }
                }
            }
        )
    }
}