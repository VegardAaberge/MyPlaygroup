package com.myplaygroup.app.feature_main.presentation.admin.create_plans

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.repository.MonthlyPlansRepository
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlansViewModel @Inject constructor(
    private val monthlyPlansRepository: MonthlyPlansRepository,
    private val usersRepository: UsersRepository
) : BaseViewModel() {

    var state by mutableStateOf(CreatePlansState())

    init {
        getMonthlyPlans()
        getUsers()
        getStandardPlans()
    }

    fun onEvent(event: CreatePlansScreenEvent) {
        when (event) {
            is CreatePlansScreenEvent.GenerateData -> {

            }
        }
    }

    private fun getMonthlyPlans() = viewModelScope.launch {
        val monthlyPlanFlow = monthlyPlansRepository.getMonthlyPlans(false)

        monthlyPlanFlow.collect { result ->
            collectResult(
                result = result,
                storeData = {
                    state = state.copy(
                        monthlyPlans = it
                    )
                }
            )
        }
    }

    private fun getUsers() = viewModelScope.launch {
        val monthlyPlanFlow = usersRepository.getAllUsers(false)

        monthlyPlanFlow.collect { result ->
            collectResult(
                result = result,
                storeData = {
                    state = state.copy(
                        users = it
                    )
                }
            )
        }
    }

    private fun getStandardPlans() = viewModelScope.launch {
        val standardPlanFlow = monthlyPlansRepository.getStandardPlans(false)

        standardPlanFlow.collect { result ->
            collectResult(
                result = result,
                storeData = {
                    state = state.copy(
                        standardPlans = it
                    )
                }
            )
        }
    }

    private fun <T> collectResult(result: Resource<T>, storeData : (T) -> Unit) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                storeData(result.data!!)
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