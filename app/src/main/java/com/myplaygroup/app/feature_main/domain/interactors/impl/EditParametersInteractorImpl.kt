package com.myplaygroup.app.feature_main.domain.interactors.impl

import com.myplaygroup.app.core.data.mapper.*
import com.myplaygroup.app.core.domain.validation.PhoneNumberValidator
import com.myplaygroup.app.core.domain.validation.ProfileNameValidator
import com.myplaygroup.app.core.domain.validation.ValidationResult
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.local.MainDao
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType.*
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.domain.interactors.EditParametersInteractor
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.model.ParameterItem
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class EditParametersInteractorImpl @Inject constructor(
    private val dao: MainDao
) : EditParametersInteractor {
    private val phoneNumberValidator = PhoneNumberValidator()
    private val profileNameValidator = ProfileNameValidator()

    override suspend fun fetchParameterItems(
        type: ParametersType,
        id: String
    ) : Resource<List<ParameterItem>> {

        val parameterItems = when(type){
            ParametersType.CLASSES -> {
                val dailyClass = dao.getDailyClassById(id.toLong()).toDailyClass()

                listOf(
                    ParameterItem(HIDDEN, dailyClass::id.name, dailyClass.id),
                    ParameterItem(INFO, dailyClass::classType.name, dailyClass.classType),
                    ParameterItem(INFO, dailyClass::dayOfWeek.name, dailyClass.dayOfWeek),
                    ParameterItem(INFO, dailyClass::kids.name, dailyClass.kids),
                    ParameterItem(DATE, dailyClass::date.name, dailyClass.date),
                    ParameterItem(TIME, dailyClass::startTime.name, dailyClass.startTime),
                    ParameterItem(TIME, dailyClass::endTime.name, dailyClass.endTime),
                    ParameterItem(SWITCH, dailyClass::cancelled.name, dailyClass.cancelled),
                )
            }
            ParametersType.PLANS -> {
                val monthlyPlan = dao.getMonthlyPlanById(id).toMonthlyPlan()

                listOf(
                    ParameterItem(HIDDEN, monthlyPlan::id.name, monthlyPlan.clientId),
                    ParameterItem(INFO, monthlyPlan::planName.name, monthlyPlan.planName),
                    ParameterItem(INFO, monthlyPlan::startDate.name, monthlyPlan.startDate),
                    ParameterItem(INFO, monthlyPlan::endDate.name, monthlyPlan.endDate),
                    ParameterItem(INFO, monthlyPlan::daysOfWeek.name, monthlyPlan.daysOfWeek),
                    ParameterItem(STRING, monthlyPlan::kidName.name, monthlyPlan.kidName),
                    ParameterItem(NUMBER, monthlyPlan::planPrice.name, monthlyPlan.planPrice),
                    ParameterItem(SWITCH, monthlyPlan::cancelled.name, monthlyPlan.cancelled),
                )
            }
            ParametersType.USERS -> {
                val appUser = dao.getAppUserById(id).toAppUser()

                listOf(
                    ParameterItem(HIDDEN, appUser::id.name, appUser.clientId),
                    ParameterItem(INFO, appUser::username.name, appUser.username),
                    ParameterItem(STRING, appUser::phoneNumber.name, appUser.phoneNumber),
                    ParameterItem(STRING, appUser::profileName.name, appUser.profileName),
                    ParameterItem(SWITCH, appUser::profileCreated.name, appUser.profileCreated),
                    ParameterItem(SWITCH, appUser::locked.name, appUser.locked),
                    ParameterItem(SWITCH, appUser::resetPassword.name, appUser.resetPassword),
                )
            }
            ParametersType.UNDEFINED -> {
                listOf()
            }
        }

        return Resource.Success(parameterItems)
    }

    override fun validateParameters(
        parameterItems: List<ParameterItem>,
        type: ParametersType,
    ) : MutableList<ParameterItem> {

        val items = parameterItems.toMutableList()

        when(type){
            ParametersType.CLASSES -> {

            }
            ParametersType.PLANS -> {

            }
            ParametersType.USERS -> {
                items.updateError(AppUser::phoneNumber.name){
                    phoneNumberValidator(it.toString())
                }
                items.updateError(AppUser::profileName.name){
                    profileNameValidator(it.toString())
                }
            }
            ParametersType.UNDEFINED -> {

            }
        }

        return items;
    }

    override suspend fun storeParameterItems(
        parameterItems: List<ParameterItem>,
        type: ParametersType,
    ): Resource<Unit> {

        return try {
            val id = parameterItems.firstOrNull { x -> x.key == "id" }?.value.toString() ?: ""

            storeParameterItemBody(
                id = id,
                parameterItems = parameterItems,
                type = type,
            )

        }catch (t : Throwable){
            t.printStackTrace()
            Resource.Error(t.localizedMessage ?: "Unknown Error")
        }
    }

    private suspend fun storeParameterItemBody(
        id: String,
        parameterItems: List<ParameterItem>,
        type: ParametersType,
    ) : Resource<Unit> {

        return when(type){
            ParametersType.CLASSES -> {
                val dailyClass = dao.getDailyClassById(id.toLong()).toDailyClass()

                val date = parameterItems.getValue(dailyClass::date.name, dailyClass.date)
                val startTime = parameterItems.getValue(dailyClass::startTime.name, dailyClass.startTime)
                val endTime = parameterItems.getValue(dailyClass::endTime.name, dailyClass.endTime)
                val cancelled = parameterItems.getValue(dailyClass::cancelled.name, dailyClass.cancelled)

                val dailyClassEntity = dailyClass.copy(
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    cancelled = cancelled,
                    modified = true
                ).toDailyClassEntity()

                dao.insertDailyClass(dailyClassEntity)
                Resource.Success()
            }
            ParametersType.PLANS -> {
                val monthlyPlan = dao.getMonthlyPlanById(id).toMonthlyPlan()

                val kidName = parameterItems.getValue(monthlyPlan::kidName.name, monthlyPlan.kidName)
                val planPrice = parameterItems.getValue(monthlyPlan::planPrice.name, monthlyPlan.planPrice)
                val cancelled = parameterItems.getValue(monthlyPlan::cancelled.name, monthlyPlan.cancelled)

                val monthlyPlanEntity = monthlyPlan.copy(
                    kidName = kidName,
                    planPrice = planPrice,
                    cancelled = cancelled,
                    modified = true
                ).toMonthlyPlanEntity()

                dao.insertMonthlyPlan(monthlyPlanEntity)
                Resource.Success()
            }
            ParametersType.USERS -> {
                val appUser = dao.getAppUserById(id).toAppUser()

                val phoneNumber = parameterItems.getValue(appUser::phoneNumber.name, appUser.phoneNumber)
                val profileName = parameterItems.getValue(appUser::profileName.name, appUser.profileName)
                val profileCreated = parameterItems.getValue(appUser::profileCreated.name, appUser.profileCreated)
                val locked = parameterItems.getValue(appUser::locked.name, appUser.locked)
                val resetPassword = parameterItems.getValue(appUser::resetPassword.name, appUser.resetPassword)

                val appUserEntity = appUser.copy(
                    phoneNumber = phoneNumber,
                    profileName = profileName,
                    profileCreated = profileCreated,
                    locked = locked,
                    resetPassword = resetPassword,
                    modified = true
                ).toAppUserEntity()

                dao.insertAppUser(appUserEntity)
                Resource.Success()
            }
            ParametersType.UNDEFINED -> {
                Resource.Error("Parameter type is not defined,")
            }
        }
    }

    private fun MutableList<ParameterItem>.updateError(name: String, validator: (Any) -> ValidationResult){

        val item = this.firstOrNull { x -> x.key == name } ?: return

        val error = if(!item.value.toString().isEmpty()){
            validator(item.value).errorMessage
        }else null

        val itemIndex = this.indexOf(item)
        this[itemIndex] = item.copy(
            error = error
        )
    }

    private fun <T> List<ParameterItem>.getValue(name: String, value : T) : T {
        return this.first { x -> x.key == name }.value as T
    }

    private fun List<ParameterItem>.getItem(name: String) : ParameterItem {
        return this.first { x -> x.key == name }
    }
}
