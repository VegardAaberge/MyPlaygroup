package com.myplaygroup.app.feature_main.data.repository

import android.content.Context
import com.myplaygroup.app.R
import com.myplaygroup.app.core.data.mapper.toMonthlyPlan
import com.myplaygroup.app.core.data.mapper.toMonthlyPlanEntity
import com.myplaygroup.app.core.data.remote.PlaygroupApi
import com.myplaygroup.app.core.domain.repository.TokenRepository
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.checkForInternetConnection
import com.myplaygroup.app.core.util.fetchNetworkResource
import com.myplaygroup.app.core.util.networkBoundResource
import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.data.mapper.toStandardPlan
import com.myplaygroup.app.feature_main.data.model.MonthlyPlanEntity
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.model.StandardPlan
import com.myplaygroup.app.feature_main.domain.repository.MonthlyPlansRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

class MonthlyPlansRepositoryImpl @Inject constructor(
    private val mainDatabase: MainDatabase,
    private val tokenRepository: TokenRepository,
    private val api: PlaygroupApi,
    @ApplicationContext private val context: Context
) : MonthlyPlansRepository {

    val dao = mainDatabase.mainDao()

    override suspend fun getMonthlyPlans(
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

    override suspend fun getStandardPlans(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<StandardPlan>>> {
        return networkBoundResource(
            query = {
                dao.getStandardPlans().map { x -> x.toStandardPlan() }
            },
            fetch = {
                api.getStandardPlans()
            },
            saveFetchResult = { standardPlans ->
                dao.clearStandardPlans()
                dao.insertStandardPlans(standardPlans)
                dao.getStandardPlans().map { it.toStandardPlan() }
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

    override suspend fun addMonthlyPlanToDatabase(monthlyPlan: MonthlyPlan): Resource<MonthlyPlan> {
        return try {
            val monthlyPlanEntity = monthlyPlan.toMonthlyPlanEntity()
            dao.insertMonthlyPlan(monthlyPlanEntity)
            Resource.Success(monthlyPlanEntity.toMonthlyPlan())
        } catch (t: Throwable) {
            t.printStackTrace()
            Resource.Error(t.localizedMessage ?: context.getString(R.string.error_unknown_error))
        }
    }

    override suspend fun uploadMonthlyPlans(
        unsyncedMonthlyPlans: List<MonthlyPlan>
    ): Flow<Resource<List<MonthlyPlan>>> {

        return fetchNetworkResource(
            fetch = {
                val entitiesToUpload = unsyncedMonthlyPlans.map { x -> x.clientId }
                val monthlyPlanEntities = dao.getMonthlyPlansByClientId(entitiesToUpload)
                api.uploadMonthlyPlans(monthlyPlanEntities)
            },
            processFetch = { monthlyPlans ->
                dao.clearAllMonthlyPlans()
                dao.insertMonthlyPlans(monthlyPlans)
                dao.getMonthlyPlans().map { it.toMonthlyPlan() }
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

    override suspend fun updateClassInfo(): Resource<List<MonthlyPlan>> {
        return try {
            val monthlyPlans = dao.getMonthlyPlans()

            return updateClassInfo(monthlyPlans)
        }catch(t: Throwable) {
            t.printStackTrace()
            Resource.Error(t.localizedMessage ?: context.getString(R.string.error_unknown_error))
        }
    }

    override suspend fun updateClassInfo(monthlyPlan: MonthlyPlan): Resource<List<MonthlyPlan>> {
        return try {
            val monthlyPlanEntity = dao.getMonthlyPlanById(monthlyPlan.clientId)

            return updateClassInfo(listOf(monthlyPlanEntity))
        }catch(t: Throwable) {
            t.printStackTrace()
            Resource.Error(t.localizedMessage ?: context.getString(R.string.error_unknown_error))
        }
    }

    suspend fun updateClassInfo(monthlyPlans: List<MonthlyPlanEntity>) : Resource<List<MonthlyPlan>> {
        val standardPlans = dao.getStandardPlans()

        monthlyPlans.forEach { monthlyPlan ->
            standardPlans.firstOrNull { it.name == monthlyPlan.planName }?.let { standardPlan ->
                val classesEntities = dao.getDailyClassesForUser(
                    startDate = monthlyPlan.startDate,
                    endDate = monthlyPlan.endDate,
                    classType = standardPlan.type,
                    daysOfWeek = monthlyPlan.daysOfWeek
                )

                val newMonthlyPlan = monthlyPlan.copy(
                    availableClasses = classesEntities.size,
                    cancelledClasses = classesEntities.count { it.cancelled }
                )

                if(newMonthlyPlan.hashCode() != monthlyPlan.hashCode()){
                    dao.insertMonthlyPlan(newMonthlyPlan)
                }
            }
        }

        val monthlyPlanEntities = dao.getMonthlyPlansByClientId(monthlyPlans.map { it.clientId })
        val newMonthlyPlans = monthlyPlanEntities.map { it.toMonthlyPlan() }

        return Resource.Success(newMonthlyPlans)
    }
}