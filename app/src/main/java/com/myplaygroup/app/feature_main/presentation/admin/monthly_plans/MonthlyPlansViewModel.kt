package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

import android.os.SystemClock
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
class MonthlyPlansViewModel @Inject constructor(
    private val repository: MonthlyPlansRepository
) : BaseViewModel() {

    private var lastRefresh: Long = 0

    var state by mutableStateOf(MonthlyPlansState())

    init {
        fetchData(true)
    }

    fun onEvent(event: MonthlyPlansScreenEvent) {
        when (event) {
            is MonthlyPlansScreenEvent.RefreshData -> {
                fetchData(false)
            }
        }
    }

    fun fetchData(fetchFromRemote: Boolean){
        if (SystemClock.elapsedRealtime() - lastRefresh < 1000){
            return;
        }
        lastRefresh = SystemClock.elapsedRealtime()

        viewModelScope.launch(Dispatchers.IO) {
            repository
                .getAllMonthlyPlans(fetchFromRemote)
                .collect{ collectMonthlyPlans(it) }
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
