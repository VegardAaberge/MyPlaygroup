package com.myplaygroup.app.feature_main.presentation.admin.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import com.myplaygroup.app.feature_main.domain.repository.DailyClassesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassesViewModel @Inject constructor(
    private val repository: DailyClassesRepository
) : BaseViewModel() {

    var state by mutableStateOf(ClassesState())

    init {
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
            is ClassesScreenEvent.AddNewDateToCreate -> {
                state = state.copy(
                    datesToCreate = event.createDate
                )
            }
        }
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