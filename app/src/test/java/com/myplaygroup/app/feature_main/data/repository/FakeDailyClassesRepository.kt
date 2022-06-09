package com.myplaygroup.app.feature_main.data.repository

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.enums.DailyClassType
import com.myplaygroup.app.feature_main.domain.repository.DailyClassesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class FakeDailyClassesRepository : DailyClassesRepository {

    var dailyClasses = mutableListOf<DailyClass>(
        DailyClass(
            id = 1,
            classType = DailyClassType.MORNING.name,
            date = LocalDate.now().minusDays(1),
            endTime = LocalTime.of(11, 30),
            startTime = LocalTime.of(9, 30),
            dayOfWeek = DayOfWeek.MONDAY
        ),
        DailyClass(
            id = 2,
            classType = DailyClassType.EVENING.name,
            date = LocalDate.now().plusDays(1),
            endTime = LocalTime.of(19, 30),
            startTime = LocalTime.of(17, 30),
            dayOfWeek = DayOfWeek.WEDNESDAY
        )
    )

    override fun getAllDailyClasses(fetchFromRemote: Boolean): Flow<Resource<List<DailyClass>>> {
        return flow {
            emit(Resource.Success(dailyClasses))
        }
    }

    override fun uploadDailyClasses(unsyncedClasses: List<DailyClass>): Flow<Resource<List<DailyClass>>> {
        return flow {
            val startIndex = dailyClasses.maxOf { x -> x.id } + 1

            unsyncedClasses.indices.forEach { index ->
                dailyClasses.add(
                    unsyncedClasses[index].copy(id = index + startIndex)
                )
            }

            emit(Resource.Success(dailyClasses))
        }
    }
}