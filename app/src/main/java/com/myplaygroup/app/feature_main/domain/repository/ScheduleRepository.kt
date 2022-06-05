package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.remote.response.MonthlyPlansResponse
import com.myplaygroup.app.feature_main.domain.model.UserSchedule
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun getUsersMonthlyPlans(
        fetchFromRemote: Boolean
    ) : Flow<Resource<UserSchedule>>
}