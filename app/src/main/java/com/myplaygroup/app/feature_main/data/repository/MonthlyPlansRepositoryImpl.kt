package com.myplaygroup.app.feature_main.data.repository

import android.app.Application
import com.myplaygroup.app.core.data.mapper.toMonthlyPlan
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.repository.MonthlyPlansRepository
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class MonthlyPlansRepositoryImpl @Inject constructor(
    private val mainDatabase: MainDatabase,
    private val tokenRepository: TokenRepository,
    private val api: PlaygroupApi,
    private val app: Application
) : MonthlyPlansRepository {

    val dao = mainDatabase.mainDao()

    override fun getAllMonthlyPlans(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<MonthlyPlan>>> {

        return networkBoundResource(
            query = {
                dao.getMonthlyPlans().map { x -> x.toMonthlyPlan() }
            },
            fetch = {
                api.getMonthlyPlans()
            },
            saveFetchResult = { monthlyPlans ->
                dao.clearSyncedMonthlyPlans()
                dao.insertMonthlyPlans(monthlyPlans)
                dao.getMonthlyPlans().map { it.toMonthlyPlan() }
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