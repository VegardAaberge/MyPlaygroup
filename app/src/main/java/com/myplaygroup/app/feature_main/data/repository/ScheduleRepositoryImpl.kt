package com.myplaygroup.app.feature_main.data.repository

import android.app.Application
import com.myplaygroup.app.core.data.mapper.toDailyClass
import com.myplaygroup.app.core.data.mapper.toMonthlyPlan
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.domain.model.UserSchedule
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
    ): Flow<Resource<UserSchedule>> {

        return networkBoundResource(
            query = {
                val dailyClasses = dao.getDailyClasses().map { x -> x.toDailyClass() }
                val monthlyPlans = dao.getMonthlyPlans().map { x -> x.toMonthlyPlan() }

                UserSchedule(
                    dailyClasses = dailyClasses,
                    monthlyPlans = monthlyPlans
                )
            },
            fetch = {
                api.getSchedule()
            },
            saveFetchResult = { schedule ->
                dao.clearAllMonthlyPlans()
                dao.insertMonthlyPlans(schedule.monthlyPlans)

                dao.clearAllDailyClasses()
                dao.insertDailyClasses(schedule.dailyClasses)

                val dailyClasses2 = dao.getDailyClasses().map { x -> x.toDailyClass() }
                val monthlyPlans2 = dao.getMonthlyPlans().map { x -> x.toMonthlyPlan() }

                UserSchedule(
                    dailyClasses = dailyClasses2,
                    monthlyPlans = monthlyPlans2
                )
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