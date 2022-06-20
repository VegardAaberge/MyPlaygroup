package com.myplaygroup.app.feature_main.presentation.admin.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.enums.DailyClassType
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.repository.DailyClassesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    lateinit var dailyClassFlow : MutableStateFlow<List<DailyClass>>

    fun init(dailyClassFlow: MutableStateFlow<List<DailyClass>>) {
        this.dailyClassFlow = dailyClassFlow

        dailyClassFlow.onEach { dailyClasses ->
            state = state.copy(
                dailyClasses = dailyClasses
            )
        }.launchIn(viewModelScope)

        setDailyClassType(DailyClassType.MORNING)
    }

    fun onEvent(event: ClassesScreenEvent) {
        when (event) {
            is ClassesScreenEvent.RefreshData -> {
                viewModelScope.launch(Dispatchers.IO){
                    repository
                        .getAllDailyClasses(false)
                        .collect{ collectDailyClasses(it, false) }
                }
            }
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
                        .collect{ collectDailyClasses(it, true) }
                }
            }
            is ClassesScreenEvent.SubmitSelectedClassTapped -> {

                state.selectedClass?.let { selectedClass ->

                    val newDailyClasses = state.dailyClasses.toMutableList()

                    if(event.delete){
                        newDailyClasses.remove(selectedClass)
                    }else{
                        val newDailyClass = selectedClass.copy(
                            startTime = event.startTime,
                            endTime = event.endTime,
                            date = event.classDate,
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
        if(isBusy)
            return emptyList()
        return state.dailyClasses.filter { x -> x.modified }
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

        val isWeekend = type == DailyClassType.WEEKEND
        val startTime = if (type == DailyClassType.MORNING || isWeekend) {
            LocalTime.of(9, 30)
        } else LocalTime.of(17, 30)

        val endTime = startTime.plusHours(2)

        val weekdays : EnumMap<DayOfWeek, Boolean> = EnumMap(DayOfWeek::class.java)
        weekdays.put(DayOfWeek.MONDAY, !isWeekend)
        weekdays.put(DayOfWeek.TUESDAY, !isWeekend)
        weekdays.put(DayOfWeek.WEDNESDAY, !isWeekend)
        weekdays.put(DayOfWeek.THURSDAY, !isWeekend)
        weekdays.put(DayOfWeek.FRIDAY, !isWeekend)
        weekdays.put(DayOfWeek.SATURDAY, isWeekend)

        state = state.copy(
            startTime = startTime,
            endTime = endTime,
            dailyClassType = type,
            weekdays = weekdays
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

    private fun collectDailyClasses(result: Resource<List<DailyClass>>, fetchFromRemote: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                if(fetchFromRemote){
                    dailyClassFlow.emit(result.data!!)
                }else{
                    state = state.copy(
                        dailyClasses = result.data!!
                    )
                }
            }
            is Resource.Error -> {
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
            is Resource.Loading -> {
                if(fetchFromRemote){
                    isBusy(result.isLoading)
                }
            }
        }
    }
}