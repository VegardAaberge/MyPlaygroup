package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import kotlinx.coroutines.flow.Flow

interface DailyClassesRepository {

    fun getAllDailyClasses(
        fetchFromRemote: Boolean
    ) : Flow<Resource<List<DailyClass>>>

    suspend fun getDailyClassesForPlan(
        monthlyPlan: MonthlyPlan,
    ) : Resource<List<DailyClass>>

    fun uploadDailyClasses(
        unsyncedClasses: List<DailyClass>
    ): Flow<Resource<List<DailyClass>>>
}