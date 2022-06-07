package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import kotlinx.coroutines.flow.Flow

interface MonthlyPlansRepository {

    fun getAllMonthlyPlans(
        fetchFromRemote: Boolean
    ) : Flow<Resource<List<MonthlyPlan>>>
}