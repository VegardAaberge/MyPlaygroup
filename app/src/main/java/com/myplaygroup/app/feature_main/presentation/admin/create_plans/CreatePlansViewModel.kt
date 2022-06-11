package com.myplaygroup.app.feature_main.presentation.admin.create_plans

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.repository.MonthlyPlansRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlansViewModel @Inject constructor(
    private val monthlyPlansRepository: MonthlyPlansRepository
) : BaseViewModel() {

    var state by mutableStateOf(CreatePlansState())

    init {
        viewModelScope.launch {
            monthlyPlansRepository
                .getAllMonthlyPlans(false)
                .collect { collectMonthlyPlans(it) }
        }
    }

    fun onEvent(event: CreatePlansScreenEvent) {
        when (event) {
            is CreatePlansScreenEvent.GenerateData -> {

            }
        }
    }

    private fun collectMonthlyPlans(result: Resource<List<MonthlyPlan>>) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                state = state.copy(
                    monthlyPlans = result.data!!
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