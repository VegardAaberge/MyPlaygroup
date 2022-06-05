package com.myplaygroup.app.feature_main.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.domain.model.DailyClass
import com.myplaygroup.app.feature_admin.presentation.classes.ClassesScreenEvent
import com.myplaygroup.app.feature_admin.presentation.classes.ClassesState
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

): ViewModel() {

    lateinit var mainViewModel: MainViewModel

    var state by mutableStateOf(HomeState())

    init {
        state = state.copy(
            dailyClasses = listOf(
                DailyClass(
                    id = "1",
                    cancelled = false,
                    classType = "Morning Group",
                    date = LocalDate.now(),
                    endTime = "11:30",
                    startTime = "9:30"
                ),
                DailyClass(
                    id = "2",
                    cancelled = false,
                    classType = "Evening Group",
                    date = LocalDate.now(),
                    endTime = "17:30",
                    startTime = "19:30"
                )
            )
        )
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
}