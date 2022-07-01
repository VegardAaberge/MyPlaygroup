package com.myplaygroup.app.feature_main.data.repository

import android.content.Context
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.mapper.toDailyClass
import com.myplaygroup.app.core.data.mapper.toMonthlyPlan
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.data.mapper.toPayment
import com.myplaygroup.app.feature_main.domain.model.UserSchedule
import com.myplaygroup.app.feature_main.domain.repository.ScheduleRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val api: PlaygroupApi,
    private val mainDatabase: MainDatabase,
    private val tokenRepository: TokenRepository,
    @ApplicationContext private val context: Context
) : ScheduleRepository {

    private val dao = mainDatabase.mainDao()

    override suspend fun getUsersMonthlyPlans(
        fetchFromRemote: Boolean
    ): Flow<Resource<UserSchedule>> {

        return networkBoundResource(
            query = {
                val dailyClasses = dao.getDailyClasses().map { x -> x.toDailyClass() }
                val monthlyPlans = dao.getMonthlyPlans().map { x -> x.toMonthlyPlan() }
                val payments = dao.getPayments().map { x -> x.toPayment() }

                UserSchedule(
                    dailyClasses = dailyClasses,
                    monthlyPlans = monthlyPlans,
                    payments = payments
                )
            },
            fetch = {
                api.getSchedule()
            },
            saveFetchResult = { schedule ->
                dao.clearAllMonthlyPlans()
                dao.insertMonthlyPlans(schedule.monthlyPlans)

                dao.clearAllPayments()

                dao.insertPayments(schedule.payments)

                dao.clearAllDailyClasses()
                dao.insertDailyClasses(schedule.dailyClasses)

                val dailyClasses2 = dao.getDailyClasses().map { x -> x.toDailyClass() }
                val monthlyPlans2 = dao.getMonthlyPlans().map { x -> x.toMonthlyPlan() }
                val payments2 = dao.getPayments().map { x -> x.toPayment() }

                UserSchedule(
                    dailyClasses = dailyClasses2,
                    monthlyPlans = monthlyPlans2,
                    payments = payments2
                )
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
}