package com.myplaygroup.app.feature_main.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.data.mapper.toDailyClass
import com.myplaygroup.app.core.data.mapper.toDailyClassEntity
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.domain.model.DailyClass
import com.myplaygroup.app.feature_admin.presentation.classes.ClassesScreenEvent
import com.myplaygroup.app.feature_admin.presentation.classes.ClassesState
import com.myplaygroup.app.feature_main.data.remote.response.MonthlyPlansResponse
import com.myplaygroup.app.feature_main.domain.repository.ScheduleRepository
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
): ViewModel() {

    lateinit var mainViewModel: MainViewModel

    var state by mutableStateOf(HomeState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository
                .getUsersMonthlyPlans(true)
                .collect { collectMonthlySchedule(it) }
        }
    }

    fun onEvent(event: ClassesScreenEvent){
        when(event){
            is ClassesScreenEvent.SelectedNewDate -> {
                state = state.copy(
                    selectedDate = event.selectedDate
                )
            }
        }
    }

    private fun collectMonthlySchedule(result: Resource<MonthlyPlansResponse>) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                state = state.copy(
                    dailyClasses = result.data!!.dailyClasses.map { it.toDailyClassEntity().toDailyClass() }
                )
            }
            is Resource.Error -> {
                mainViewModel.setUIEvent(
                    BaseViewModel.UiEvent.ShowSnackbar(result.message!!)
                )
            }
            is Resource.Loading -> {
                state = state.copy(isLoading = result.isLoading)
            }
        }
    }
}