package com.myplaygroup.app.feature_main.domain.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import kotlinx.coroutines.flow.Flow

interface DailyClassesRepository {

    fun getAllDailyClasses(
        fetchFromRemote: Boolean
    ) : Flow<Resource<List<DailyClass>>>

    fun createDailyClasses() : Flow<Resource<Unit>>
}