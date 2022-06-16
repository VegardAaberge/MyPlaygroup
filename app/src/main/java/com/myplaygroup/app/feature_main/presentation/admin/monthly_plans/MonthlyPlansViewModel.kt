package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonthlyPlansViewModel @Inject constructor(
    private val repository: MonthlyPlansRepository
) : BaseViewModel() {

    var state by mutableStateOf(MonthlyPlansState())

    fun init(userFlow: MutableStateFlow<List<MonthlyPlan>>) {
        userFlow.onEach { monthlyPlans ->
            state = state.copy(
                monthlyPlans = getGroupedData(monthlyPlans)
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MonthlyPlansScreenEvent) {
        when (event) {
            is MonthlyPlansScreenEvent.RefreshData -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository
                        .getMonthlyPlans(false)
                        .collect{ collectMonthlyPlans(it, false) }
                }
            }
            is MonthlyPlansScreenEvent.UploadMonthlyPlans -> {
                val unsyncedMonthlyPlans = getUnsyncedMonthlyPlans()
                viewModelScope.launch(Dispatchers.IO) {
                    repository
                        .uploadMonthlyPlans(unsyncedMonthlyPlans)
                        .collect{ collectMonthlyPlans(it, true) }
                }
            }
        }
    }

    private fun collectMonthlyPlans(result: Resource<List<MonthlyPlan>>, fetchFromRemote: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                state = state.copy(
                    monthlyPlans = getGroupedData(result.data!!)
                )
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

    fun getUnsyncedMonthlyPlans(): List<MonthlyPlan> {
        if(isBusy)
            return emptyList()
        return state.monthlyPlans.flatMap { x -> x.value }.filter { x -> x.modified }
    }

    fun getGroupedData(data: List<MonthlyPlan>) : Map<String, List<MonthlyPlan>> {
        return data
            .sortedByDescending { x -> x.startDate }
            .groupBy { x -> "${x.startDate.month} ${x.startDate.year}" }
    }
}
