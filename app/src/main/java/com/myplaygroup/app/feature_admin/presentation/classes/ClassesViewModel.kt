package com.myplaygroup.app.feature_admin.presentation.classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_admin.domain.model.DailyClass
import com.myplaygroup.app.feature_admin.domain.repository.DailyClassesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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