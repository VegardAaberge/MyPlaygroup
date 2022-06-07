package com.myplaygroup.app.feature_main.presentation.admin.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.DailyClassType
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.repository.DailyClassesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
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

    fun onEvent(event: ClassesScreenEvent) {
        when (event) {
            is ClassesScreenEvent.ToggleCreateClassesSection -> {
                state = state.copy(isCreateVisible = !state.isCreateVisible)
            }
            is ClassesScreenEvent.SelectedNewDate -> {
                state = state.copy(selectedDate = event.selectedDate)
            }
            is ClassesScreenEvent.StartTimeChanged -> {
                state = state.copy(startTime = event.startTime)
            }
            is ClassesScreenEvent.EndTimeChanged -> {
                state = state.copy(endTime = event.endTime)
            }
            is ClassesScreenEvent.ClassSelected -> {
                state = state.copy(selectedClass = event.dailyClass)
            }
            is ClassesScreenEvent.DialogDismissed -> {
                state = state.copy(selectedClass = null)
            }
            is ClassesScreenEvent.WeekdayChanged -> {
                setWeekdays(event.dayOfWeek)
            }
            is ClassesScreenEvent.ClassChanged -> {
                setDailyClassType(event.type)
            }
            is ClassesScreenEvent.GenerateClassesTapped -> {
                state = state.copy(isCreateVisible = false)
                createDailyClasses()
            }
            is ClassesScreenEvent.UploadCreatedClasses -> {
                val unsyncedClasses = getUnsyncedDailyClasses()
                viewModelScope.launch(Dispatchers.IO) {
                    repository
                        .uploadDailyClasses(unsyncedClasses)
                        .collect{ collectDailyClasses(it) }
                }
            }
            is ClassesScreenEvent.SubmitSelectedClassTapped -> {

                state.selectedClass?.let { selectedClass ->

                    val shouldDelete = selectedClass.id == -1L && event.cancelled
                    val newDailyClasses = state.dailyClasses.toMutableList()

                    if(shouldDelete){
                        newDailyClasses.remove(selectedClass)
                    }else{
                        val newDailyClass = selectedClass.copy(
                            startTime = event.startTime,
                            endTime = event.endTime,
                            date = event.classDate,
                            cancelled = event.cancelled,
                            modified = true
                        )

                        Collections.replaceAll(newDailyClasses, selectedClass, newDailyClass)
                    }

                    state = state.copy(
                        dailyClasses = newDailyClasses,
                        selectedClass = null
                    )
                }
            }
        }
    }

    fun getUnsyncedDailyClasses() : List<DailyClass> {
        return state.dailyClasses.filter { x -> x.id == -1L || x.modified }
    }

    private fun initWeekdays(){
        val weekdays = state.weekdays
        weekdays.put(DayOfWeek.MONDAY, true)
        weekdays.put(DayOfWeek.TUESDAY, true)
        weekdays.put(DayOfWeek.WEDNESDAY, true)
        weekdays.put(DayOfWeek.THURSDAY, true)
        weekdays.put(DayOfWeek.FRIDAY, true)
        weekdays.put(DayOfWeek.SATURDAY, false)
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

    private fun setDailyClassType(type: DailyClassType){
        val startTime = if (type == DailyClassType.MORNING) {
            LocalTime.of(9, 30)
        } else LocalTime.of(17, 30)

        val endTime = startTime.plusHours(2)

        state = state.copy(
            startTime = startTime,
            endTime = endTime,
            dailyClassType = type
        )
    }

    private fun createDailyClasses(){
        val weekDays = state.weekdays.filter { x -> x.value }
        val currentYear = state.calendarState!!.monthState.currentMonth.year
        val currentMonth = state.calendarState!!.monthState.currentMonth.month.value

        val dailyClasses = mutableListOf<DailyClass>()
        try {
            (1..31).forEach {
                val date = LocalDate.of(currentYear, currentMonth, it)
                if(date.dayOfWeek in weekDays){
                    dailyClasses.add(DailyClass(
                        classType = state.dailyClassType.name,
                        startTime = state.startTime,
                        endTime = state.endTime,
                        dayOfWeek = date.dayOfWeek,
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