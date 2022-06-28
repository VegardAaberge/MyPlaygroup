package com.myplaygroup.app.feature_main.domain.interactors.impl

import com.myplaygroup.app.core.data.mapper.*
import com.myplaygroup.app.core.domain.validation.ValidationResult
import com.myplaygroup.app.core.domain.validation.daily_class.DateValidator
import com.myplaygroup.app.core.domain.validation.daily_class.EndTimeValidator
import com.myplaygroup.app.core.domain.validation.daily_class.StartTimeValidator
import com.myplaygroup.app.core.domain.validation.monthly_plans.KidNameValidator
import com.myplaygroup.app.core.domain.validation.monthly_plans.PlanPriceValidator
import com.myplaygroup.app.core.domain.validation.payments.AmountValidator
import com.myplaygroup.app.core.domain.validation.user.PhoneNumberValidator
import com.myplaygroup.app.core.domain.validation.user.ProfileNameValidator
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.local.MainDao
import com.myplaygroup.app.feature_main.data.mapper.toPayment
import com.myplaygroup.app.feature_main.data.mapper.toPaymentEntity
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType.*
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.domain.interactors.EditParametersInteractor
import com.myplaygroup.app.feature_main.domain.model.*
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class EditParametersInteractorImpl @Inject constructor(
    private val dao: MainDao
) : EditParametersInteractor {

    companion object{
        const val DELETE_KEY = "Delete"
    }

    // Classes
    private val dateValidator = DateValidator()
    private val startTimeValidator = StartTimeValidator()
    private val endTimeValidator = EndTimeValidator()

    // Plans
    private val kidNameValidator = KidNameValidator()
    private val planPriceValidator = PlanPriceValidator()

    // Users
    private val phoneNumberValidator = PhoneNumberValidator()
    private val profileNameValidator = ProfileNameValidator()

    // Payments
    private val amuntValidator = AmountValidator()

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
                    ParameterItem(SWITCH, dailyClass::cancelled.name, dailyClass.cancelled)
                )
            }
            ParametersType.PLANS -> {
                val monthlyPlan = dao.getMonthlyPlanById(id).toMonthlyPlan()

                val cancelDeleteParameterItem = if(monthlyPlan.id == -1L){
                    ParameterItem(DELETE, DELETE_KEY, false)
                } else ParameterItem(SWITCH, monthlyPlan::cancelled.name, monthlyPlan.cancelled)

                val standardPlans = dao.getStandardPlans().map { it.name }.toMutableList()
                standardPlans.add(0, monthlyPlan.planName)

                listOf(
                    ParameterItem(HIDDEN, monthlyPlan::id.name, monthlyPlan.clientId),
                    ParameterItem(SWITCH, monthlyPlan::changeDays.name, monthlyPlan.changeDays),
                    ParameterItem(OPTIONS, monthlyPlan::planName.name, standardPlans, monthlyPlan.changeDays),
                    ParameterItem(DATE, monthlyPlan::startDate.name, monthlyPlan.startDate, monthlyPlan.changeDays),
                    ParameterItem(DATE, monthlyPlan::endDate.name, monthlyPlan.endDate, monthlyPlan.changeDays),
                    ParameterItem(WEEKDAYS, monthlyPlan::daysOfWeek.name, monthlyPlan.daysOfWeek, monthlyPlan.changeDays),
                    ParameterItem(STRING, monthlyPlan::kidName.name, monthlyPlan.kidName),
                    ParameterItem(NUMBER, monthlyPlan::planPrice.name, monthlyPlan.planPrice),
                    cancelDeleteParameterItem,
                )
            }
            ParametersType.USERS -> {
                val appUser = dao.getAppUserById(id).toAppUser()

                val list = mutableListOf(
                    ParameterItem(HIDDEN, appUser::id.name, appUser.clientId),
                    ParameterItem(INFO, appUser::username.name, appUser.username),
                    ParameterItem(STRING, appUser::phoneNumber.name, appUser.phoneNumber),
                    ParameterItem(STRING, appUser::profileName.name, appUser.profileName),
                    ParameterItem(SWITCH, appUser::profileCreated.name, appUser.profileCreated),
                    ParameterItem(SWITCH, appUser::locked.name, appUser.locked),
                    ParameterItem(SWITCH, appUser::resetPassword.name, appUser.resetPassword),
                )
                if(appUser.id == -1L){
                    list.add(ParameterItem(DELETE, DELETE_KEY, false))
                }
                list
            }
            ParametersType.PAYMENTS -> {
                val payment = dao.getPaymentById(id).toPayment()

                val cancelDeleteParameterItem = if(payment.id == -1L){
                    ParameterItem(DELETE, DELETE_KEY, false)
                } else ParameterItem(SWITCH, payment::cancelled.name, payment.cancelled)

                listOf(
                    ParameterItem(HIDDEN, payment::id.name, payment.clientId),
                    ParameterItem(INFO, payment::username.name, payment.username),
                    ParameterItem(DATE, payment::date.name, payment.date),
                    ParameterItem(NUMBER, payment::amount.name, payment.amount),
                    cancelDeleteParameterItem,
                )
            }
            ParametersType.UNDEFINED -> {
                listOf()
            }
        }

        return Resource.Success(parameterItems)
    }

    override suspend fun processDataChanges(
        parameterItems: List<ParameterItem>,
        type: ParametersType,
        key: String
    ): Resource<List<ParameterItem>> {
        val items = parameterItems.toMutableList()

        when(type){
            ParametersType.PLANS -> {
                if(key == MonthlyPlan::changeDays.name){
                    val changeDays = items.firstOrNull { x -> x.key == key }?.value as? Boolean ?: return Resource.Error("Key: $key not found")
                    items.updateEnabled(MonthlyPlan::planName.name, changeDays)
                    items.updateEnabled(MonthlyPlan::startDate.name, changeDays)
                    items.updateEnabled(MonthlyPlan::endDate.name, changeDays)
                    items.updateEnabled(MonthlyPlan::daysOfWeek.name, changeDays)
                }
            }
            else -> { }
        }

        return Resource.Success(items)
    }

    override fun validateParameters(
        parameterItems: List<ParameterItem>,
        type: ParametersType,
    ) : MutableList<ParameterItem> {

        val items = parameterItems.toMutableList()

        when(type){
            ParametersType.CLASSES -> {
                items.updateError(DailyClass::date.name){
                    dateValidator(it as LocalDate)
                }
                items.updateError(DailyClass::startTime.name){
                    startTimeValidator(it as LocalTime)
                }
                items.updateError(DailyClass::endTime.name){
                    val startTime = items.firstOrNull{ x -> x.key == DailyClass::startTime.name}?.value as? LocalTime ?: LocalTime.of(1970, 1, 1)
                    endTimeValidator(it as LocalTime, startTime)
                }
            }
            ParametersType.PLANS -> {
                items.updateError(MonthlyPlan::kidName.name){
                    kidNameValidator(it.toString())
                }
                items.updateError(MonthlyPlan::planPrice.name){
                    planPriceValidator(it.toString().toInt())
                }
            }
            ParametersType.USERS -> {
                items.updateError(AppUser::phoneNumber.name){
                    phoneNumberValidator(it.toString())
                }
                items.updateError(AppUser::profileName.name){
                    profileNameValidator(it.toString())
                }
            }
            ParametersType.PAYMENTS -> {
                items.updateError(Payment::amount.name){
                    amuntValidator(it.toString().toInt())
                }
                items.updateError(DailyClass::date.name){
                    dateValidator(it as LocalDate)
                }
            }
            ParametersType.UNDEFINED -> {

            }
        }

        return items
    }

    override suspend fun storeParameterItems(
        parameterItems: List<ParameterItem>,
        type: ParametersType,
    ): Resource<Unit> {

        return try {
            val id = parameterItems.firstOrNull { x -> x.key == "id" }?.value.toString()

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
                val cancelled = if(dailyClass.id != -1L) {
                    parameterItems.getValue(dailyClass::cancelled.name, dailyClass.cancelled)
                } else false

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
                val cancelled = if(monthlyPlan.id != -1L) {
                    parameterItems.getValue(monthlyPlan::cancelled.name, monthlyPlan.cancelled)
                } else false

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
            ParametersType.PAYMENTS -> {
                val payment = dao.getPaymentById(id).toPayment()

                val amount = parameterItems.getValue(payment::amount.name, payment.amount)
                val date = parameterItems.getValue(payment::date.name, payment.date)
                val cancelled = if(payment.id != -1L) {
                    parameterItems.getValue(payment::cancelled.name, payment.cancelled)
                } else false

                val paymentEntity = payment.copy(
                    amount = amount,
                    date = date,
                    cancelled = cancelled,
                    modified = true
                ).toPaymentEntity()

                dao.insertPayment(paymentEntity)
                Resource.Success()
            }
            ParametersType.UNDEFINED -> {
                Resource.Error("Parameter type is not defined,")
            }
        }
    }

    override suspend fun deleteItem(
        id: String,
        type: ParametersType
    ): Resource<Unit> {
        return when(type){
            ParametersType.USERS -> {
                dao.deleteAppUsersById(id)
                Resource.Success()
            }
            ParametersType.PLANS -> {
                dao.deleteMonthlyPlansById(id)
                Resource.Success()
            }
            ParametersType.PAYMENTS -> {
                dao.deletePaymentEntityById(id)
                Resource.Success()
            }
            else -> {
                Resource.Error("Type was invalid")
            }
        }
    }

    private fun MutableList<ParameterItem>.updateError(name: String, validator: (Any) -> ValidationResult){

        val item = this.firstOrNull { x -> x.key == name } ?: return

        val error = if(item.value.toString().isNotEmpty()){
            validator(item.value).errorMessage
        }else null

        val itemIndex = this.indexOf(item)
        this[itemIndex] = item.copy(
            error = error
        )
    }

    private fun MutableList<ParameterItem>.updateEnabled(name: String, enabled: Boolean){

        val item = this.firstOrNull { x -> x.key == name } ?: return

        val itemIndex = this.indexOf(item)
        this[itemIndex] = item.copy(
            enabled = enabled
        )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun <T> List<ParameterItem>.getValue(name: String, value : T) : T {
        return this.first { x -> x.key == name }.value as T
    }
}
