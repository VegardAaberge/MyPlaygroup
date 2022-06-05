package com.myplaygroup.app.feature_main.data.repository

import android.app.Application
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.data.mapper.toMessage
import com.myplaygroup.app.feature_main.data.mapper.toMessageEntity
import com.myplaygroup.app.feature_main.data.remote.response.MonthlyPlansResponse
import com.myplaygroup.app.feature_main.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val mainDatabase: MainDatabase,
    private val app: Application,
    private val tokenRepository: TokenRepository,
) : ScheduleRepository {

    private val dao = mainDatabase.mainDao()

    override suspend fun getUsersMonthlyPlans(
        fetchFromRemote: Boolean
    ): Flow<Resource<MonthlyPlansResponse>> {

        return networkBoundResource(
            query = {
                MonthlyPlansResponse(
                    dailyClasses = emptyList(),
                    monthlyPlans = emptyList(),
                    userCredit = 0,
                    username = "vegard"
                )
            },
            fetch = {
                api.getSchedule()
            },
            saveFetchResult = { schedule ->
                schedule
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
}