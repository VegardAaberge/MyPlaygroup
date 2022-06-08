package com.myplaygroup.app.feature_main.data.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.repository.MonthlyPlansRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.DayOfWeek

class FakeMonthlyPlansRepository : MonthlyPlansRepository {

    var monthlyPlans = mutableListOf<MonthlyPlan>(
        MonthlyPlan(
            id = 1,
            kidName = "emma",
            paid = true,
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
            kidName = "ellie",
            paid = false,
            daysOfWeek = listOf(
                DayOfWeek.MONDAY,
                DayOfWeek.FRIDAY
            ),
            planName = "Morning v2",
            planPrice = 490
        ),
    )

    override fun getAllMonthlyPlans(fetchFromRemote: Boolean): Flow<Resource<List<MonthlyPlan>>> {
        return flow {
            emit(Resource.Success(monthlyPlans))
        }
    }
}