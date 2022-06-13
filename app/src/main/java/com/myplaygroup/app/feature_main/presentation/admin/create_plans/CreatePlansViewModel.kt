package com.myplaygroup.app.feature_main.presentation.admin.create_plans

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.repository.DailyClassesRepository
import com.myplaygroup.app.feature_main.domain.repository.MonthlyPlansRepository
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class CreatePlansViewModel @Inject constructor(
    private val monthlyPlansRepository: MonthlyPlansRepository,
    private val usersRepository: UsersRepository,
    private val dailyClassesRepository: DailyClassesRepository
) : BaseViewModel() {

    private var dailyClasses : List<DailyClass> = emptyList()

    var state by mutableStateOf(CreatePlansState())

    init {
        getDailyClasses()
        getMonthlyPlans()
        getUsers()
        getStandardPlans()
    }

    fun onEvent(event: CreatePlansScreenEvent) {
        when (event) {
            is CreatePlansScreenEvent.CreateMultipleUsers -> {
                state = state.copy(createMultipleUsers = event.createMultiple)
            }
            is CreatePlansScreenEvent.UserChanged -> {
                state = state.copy(user = event.user)
            }
            is CreatePlansScreenEvent.KidChanged -> {
                state = state.copy(kid = event.kid)
            }
            is CreatePlansScreenEvent.PlanChanged -> {
                state.standardPlans.firstOrNull() { x -> x.name == event.plan }?.let { plan ->
                    state = state.copy(
                        plan = plan.name,
                        price = plan.price.toString()
                    )
                }
                calculatePrice()
            }
            is CreatePlansScreenEvent.PriceChanged -> {
                state = state.copy(price = event.price)
            }
            is CreatePlansScreenEvent.WeekdayChanged -> {
                setWeekdays(event.dayOfWeek)
                calculatePrice()
            }
            is CreatePlansScreenEvent.StartDateChanged -> {
                state = state.copy(startDate = event.startDate)
                calculatePrice()
            }
            is CreatePlansScreenEvent.EndDateChanged -> {
                state = state.copy(endDate = event.endDate)
                calculatePrice()
            }
            is CreatePlansScreenEvent.BasePlanCheckboxTapped -> {
                val basePlansSelected = state.basePlansSelected.toMutableMap()
                basePlansSelected[event.kid] = event.selected
                state = state.copy(
                    basePlansSelected = basePlansSelected
                )
            }
            is CreatePlansScreenEvent.GenerateData -> {
                if (state.createMultipleUsers) {
                    addMultiplePlansToDatabase()
                } else {
                    addMonthlyPlanToDatabase()
                }
            }
        }
    }

    private fun addMultiplePlansToDatabase() = viewModelScope.launch(Dispatchers.IO) {
        state.baseMonthlyPlans.filter { x -> state.basePlansSelected[x.kidName] ?: false }.forEach { monthlyPlan ->
            val newMonthlyPlan = monthlyPlan.copy(
                clientId = UUID.randomUUID().toString(),
                id = -1,
                startDate = state.multipleStartDate,
                endDate = state.multipleEndDate,
                cancelled = false,
                modified = true
            )

            val result = monthlyPlansRepository.addMonthlyPlanToDatabase(newMonthlyPlan)
        }

        setUIEvent(
            UiEvent.PopPage
        )
    }

    private fun addMonthlyPlanToDatabase() = viewModelScope.launch(Dispatchers.IO){
        val monthlyPlan = MonthlyPlan(
            username = state.user,
            kidName = state.kid,
            startDate = state.startDate,
            endDate = state.endDate,
            planName = state.plan,
            daysOfWeek = state.weekdays.keys.toList(),
            planPrice = state.price.toLong()
        )

        val result = monthlyPlansRepository.addMonthlyPlanToDatabase(monthlyPlan)

        if (result is Resource.Success) {
            setUIEvent(
                UiEvent.PopPage
            )
        } else if (result is Resource.Error) {
            setUIEvent(
                UiEvent.ShowSnackbar(result.message!!)
            )
        }
    }

    private fun calculatePrice() {
        val daysOfWeek = state.weekdays.filter { x -> x.value }

        val relevantClasses = dailyClasses
            .filter { x -> daysOfWeek.containsKey(x.dayOfWeek) }

        if(relevantClasses.size == 0 || state.plan.isEmpty())
            return

        val firstDay = LocalDate.of(state.startDate.year, state.startDate.month, 1)
        val lastDay = firstDay.plusMonths(1).minusDays(1);

        val classesInMonth = relevantClasses.filter { x -> x.date >= state.startDate && x.date <= state.endDate }.size.toFloat()
        val classesChosen = relevantClasses.filter { x -> x.date >= firstDay && x.date <= lastDay }.size.toFloat()
        val price = state.standardPlans.first { x -> x.name == state.plan }.price

        val adjustedPrice = price / (classesChosen / classesInMonth)
        state = state.copy(
            price = adjustedPrice.roundToInt().toString()
        )
    }

    private fun setWeekdays(dayOfWeek: DayOfWeek){
        val weekdays = state.weekdays.toMutableMap()
        val currentValue = weekdays[dayOfWeek] ?: false
        weekdays[dayOfWeek] =  !currentValue
        state = state.copy(
            weekdays = weekdays,
        )
    }

    private fun getMonthlyPlans() = viewModelScope.launch {
        val monthlyPlanFlow = monthlyPlansRepository.getMonthlyPlans(false)

        monthlyPlanFlow.collect { result ->
            collectResult(
                result = result,
                storeData = {
                    val maxDate = it.maxOfOrNull { x -> x.startDate } ?: LocalDate.now()
                    val startDate = LocalDate.of(maxDate.year, maxDate.month, 1)
                    val multipleStartDate = startDate.plusMonths(1)
                    val endDate = startDate.plusMonths(1).minusDays(1)
                    val multipleEndDate = multipleStartDate.plusMonths(1).minusDays(1)

                    val baseMonthlyPlans = it.filter { x -> x.startDate > LocalDate.now().minusMonths(3) }
                        .groupBy { x -> x.kidName }
                        .map { x -> x.value.maxByOrNull { y -> y.startDate }!! }

                    val baseMonthlyPlansSelected = baseMonthlyPlans.map { it.kidName to true }.toMap()

                    state = state.copy(
                        monthlyPlans = it,
                        startDate = startDate,
                        endDate = endDate,
                        multipleStartDate = multipleStartDate,
                        multipleEndDate = multipleEndDate,
                        baseMonthlyPlans = baseMonthlyPlans,
                        basePlansSelected = baseMonthlyPlansSelected
                    )
                }
            )
        }
    }

    private fun getDailyClasses() = viewModelScope.launch {
        val dailyClassesFlow = dailyClassesRepository.getAllDailyClasses(false)

        dailyClassesFlow.collect { result ->
            collectResult(
                result = result,
                storeData = {
                    dailyClasses = it
                }
            )
        }
    }

    private fun getUsers() = viewModelScope.launch {
        val monthlyPlanFlow = usersRepository.getAllUsers(false)

        monthlyPlanFlow.collect { result ->
            collectResult(
                result = result,
                storeData = {
                    state = state.copy(
                        users = it
                    )
                }
            )
        }
    }

    private fun getStandardPlans() = viewModelScope.launch {
        val standardPlanFlow = monthlyPlansRepository.getStandardPlans(false)

        standardPlanFlow.collect { result ->
            collectResult(
                result = result,
                storeData = {
                    state = state.copy(
                        standardPlans = it
                    )
                }
            )
        }
    }

    private fun <T> collectResult(result: Resource<T>, storeData : (T) -> Unit) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                storeData(result.data!!)
            }
            is Resource.Error -> {
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
            is Resource.Loading -> {
                state = state.copy(isLoading = result.isLoading)
            }
        }
    }
}