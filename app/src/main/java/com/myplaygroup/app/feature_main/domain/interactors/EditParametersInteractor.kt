package com.myplaygroup.app.feature_main.domain.interactors

import com.myplaygroup.app.core.data.mapper.toAppUser
import com.myplaygroup.app.core.data.mapper.toDailyClass
import com.myplaygroup.app.core.data.mapper.toMonthlyPlan
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.local.MainDao
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType.*
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.domain.model.ParameterItem
import javax.inject.Inject

class EditParametersInteractor @Inject constructor(
    private val dao: MainDao
){
    suspend fun fetchParameterItems(
        type: ParametersType,
        id: Long
    ) : Resource<List<ParameterItem>> {

        val parameterItems = when(type){
            ParametersType.CLASSES -> {
                val dailyClass = dao.getDailyClassById(id).toDailyClass()

                listOf(
                    ParameterItem(INFO, dailyClass::classType.name, dailyClass.classType),
                    ParameterItem(INFO, dailyClass::dayOfWeek.name, dailyClass.dayOfWeek),
                    ParameterItem(DATE, dailyClass::date.name, dailyClass.date),
                    ParameterItem(TIME, dailyClass::startTime.name, dailyClass.startTime),
                    ParameterItem(TIME, dailyClass::endTime.name, dailyClass.endTime),
                    ParameterItem(SWITCH, dailyClass::cancelled.name, dailyClass.cancelled),
                )
            }
            ParametersType.PLANS -> {
                val monthlyPlan = dao.getMonthlyPlanById(id).toMonthlyPlan()

                listOf(
                    ParameterItem(INFO, monthlyPlan::planName.name, monthlyPlan.planName),
                    ParameterItem(INFO, monthlyPlan::month.name, monthlyPlan.month),
                    ParameterItem(STRING, monthlyPlan::kidName.name, monthlyPlan.kidName),
                    ParameterItem(NUMBER, monthlyPlan::planPrice.name, monthlyPlan.planPrice),
                    ParameterItem(STRING, monthlyPlan::daysOfWeek.name, monthlyPlan.daysOfWeek),
                )
            }
            ParametersType.USERS -> {
                val appUser = dao.getAppUserById(id).toAppUser()

                listOf(
                    ParameterItem(INFO, appUser::username.name, appUser.username),
                    ParameterItem(STRING, appUser::phoneNumber.name, appUser.phoneNumber ?: ""),
                    ParameterItem(STRING, appUser::profileName.name, appUser.profileName ?: ""),
                    ParameterItem(SWITCH, appUser::profileCreated.name, appUser.profileCreated ?: ""),
                    ParameterItem(SWITCH, appUser::locked.name, appUser.locked ?: ""),
                )
            }
        }

        return Resource.Success(parameterItems)
    }

    suspend fun validateParameters(
        editParameters: List<ParameterItem>
    ) : List<ParameterItem> {
        return editParameters;
    }

    suspend fun storeParameterItems(
        editParameters: List<ParameterItem>
    ) : Resource<String> {
        return Resource.Success("")
    }
}
