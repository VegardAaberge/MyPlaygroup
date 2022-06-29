package com.myplaygroup.app.feature_main.data.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.model.StandardPlan
import com.myplaygroup.app.feature_main.domain.repository.MonthlyPlansRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.DayOfWeek
import java.time.LocalDate

class FakeMonthlyPlansRepository : MonthlyPlansRepository {

    var monthlyPlans = mutableListOf<MonthlyPlan>(
        MonthlyPlan(
            id = 1,
            username = "vegard",
            startDate = LocalDate.of(2022, 7, 1),
            endDate = LocalDate.of(2022, 7, 1).plusMonths(1).minusDays(1),
            kidName = "emma",
            daysOfWeek = listOf(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY
            ),
            planName = "Evening v3",
            planPrice = 790
        ),
        MonthlyPlan(
            id = 2,
            username = "vegard",
            startDate = LocalDate.of(2022, 7, 1),
            endDate = LocalDate.of(2022, 7, 1).plusMonths(1).minusDays(1),
            kidName = "ellie",
            daysOfWeek = listOf(
                DayOfWeek.MONDAY,
                DayOfWeek.FRIDAY
            ),
            planName = "Morning v2",
            planPrice = 490
        ),
    )

    override suspend fun getMonthlyPlans(fetchFromRemote: Boolean): Flow<Resource<List<MonthlyPlan>>> {
        return flow {
            emit(Resource.Success(monthlyPlans))
        }
    }

    override suspend fun getStandardPlans(fetchFromRemote: Boolean): Flow<Resource<List<StandardPlan>>> {
        TODO("Not yet implemented")
    }

    override suspend fun addMonthlyPlanToDatabase(monthlyPlan: MonthlyPlan): Resource<MonthlyPlan> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadMonthlyPlans(unsyncedMonthlyPlans: List<MonthlyPlan>): Flow<Resource<List<MonthlyPlan>>> {
        TODO("Not yet implemented")
    }
}