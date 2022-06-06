package com.myplaygroup.app.feature_main.presentation.admin.classes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.enums.DailyClassType
import com.myplaygroup.app.feature_main.domain.enums.DayOfWeek
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.repository.DailyClassesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ClassesViewModel @Inject constructor(
    private val repository: DailyClassesRepository
) : BaseViewModel() {

    var state by mutableStateOf(ClassesState())

    init {
        initWeekdays()

        viewModelScope.launch(Dispatchers.IO) {
            repository
                .getAllDailyClasses(true)
                .collect{ collectDailyClasses(it) }
        }
    }

    fun onEvent(event: ClassesScreenEvent){
        when(event){
            is ClassesScreenEvent.ToggleCreateClassesSection -> {
                state = state.copy(isCreateVisible = !state.isCreateVisible)
            }
            is ClassesScreenEvent.SelectedNewDate -> {
                state = state.copy(
                    selectedDate = event.selectedDate
                )
            }
            is ClassesScreenEvent.ClassChanged -> {
                val startTime = if(event.type == DailyClassType.MORNING){
                    LocalTime.of(9, 30)
                } else LocalTime.of(17, 30)

                val endTime = startTime.plusHours(2)

                state = state.copy(
                    startTime = startTime,
                    endTime = endTime,
                    dailyClassType = event.type
                )
            }
            is ClassesScreenEvent.StartTimeChanged -> {
                state = state.copy(startTime = event.startTime)
            }
            is ClassesScreenEvent.EndTimeChanged -> {
                state = state.copy(endTime = event.endTime)
            }
            is ClassesScreenEvent.WeekdayChanged -> {
                setWeekdays(event.dayOfWeek)
            }
            is ClassesScreenEvent.GenerateClassesTapped -> {
                state = state.copy(isCreateVisible = false)
                createDailyClasses()
            }
        }
    }

    private fun initWeekdays(){
        val weekdays = state.weekdays
        weekdays.put(DayOfWeek.MON, true)
        weekdays.put(DayOfWeek.TUE, true)
        weekdays.put(DayOfWeek.WED, true)
        weekdays.put(DayOfWeek.THU, true)
        weekdays.put(DayOfWeek.FRI, true)
        weekdays.put(DayOfWeek.SAT, false)
        state = state.copy(
            weekdays = weekdays
        )
    }

    private fun setWeekdays(dayOfWeek: DayOfWeek){
        val weekdays = state.weekdays
        val currentValue = weekdays[dayOfWeek] ?: false
        weekdays.put(dayOfWeek, !currentValue)
        state = state.copy(
            weekdays = weekdays
        )
    }

    private fun createDailyClasses(){
        val weekOrdinals = state.weekdays.filter { x -> x.value }.keys.map { x -> x.ordinal }
        val currentYear = state.calendarState!!.monthState.currentMonth.year
        val currentMonth = state.calendarState!!.monthState.currentMonth.month.value

        val dailyClasses = mutableListOf<DailyClass>()
        try {
            (1..31).forEach {
                val date = LocalDate.of(currentYear, currentMonth, it)
                if(date.dayOfWeek.ordinal in weekOrdinals){
                    dailyClasses.add(DailyClass(
                        classType = state.dailyClassType.name,
                        startTime = state.startTime,
                        endTime = state.endTime,
                        date = date,
                    ))
                }
            }
        }catch(_: DateTimeException) { }

        dailyClasses.addAll(state.dailyClasses)

        state = state.copy(
            dailyClasses = dailyClasses
        )
    }

    private fun collectDailyClasses(result: Resource<List<DailyClass>>) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                state = state.copy(
                    dailyClasses = result.data!!
                )
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