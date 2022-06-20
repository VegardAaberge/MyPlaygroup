package com.myplaygroup.app.feature_main.presentation.admin.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.interactors.MainValidationInteractors
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.model.Payment
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UsersRepository,
    private val validators: MainValidationInteractors
) : BaseViewModel() {

    var state by mutableStateOf(UsersState())

    lateinit var userFlow: MutableStateFlow<List<AppUser>>
    var payments: List<Payment> = emptyList()
    var monthlyPlans: List<MonthlyPlan> = emptyList()


    fun init(
        userFlow: MutableStateFlow<List<AppUser>>,
        paymentFlow: MutableStateFlow<List<Payment>>,
        monthlyPlanFlow: MutableStateFlow<List<MonthlyPlan>>,
    ) {
        this.userFlow = userFlow

        combine(userFlow, paymentFlow, monthlyPlanFlow){ users, payments, plans ->
            this.payments = payments
            this.monthlyPlans = plans
            state = state.copy(
                appUsers = calculateAndModifyBalance(users)
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: UsersScreenEvent) {
        when (event) {
            is UsersScreenEvent.RefreshData -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository
                        .getAllUsers(false)
                        .collect{ collectAppUsers(it, false) }
                }
            }
            is UsersScreenEvent.CreateUserDialog -> {
                state = state.copy(
                    showCreateUser = event.show,
                    createErrorMessage = null
                )
            }
            is UsersScreenEvent.CreateUser -> {
                val usernameResult = validators.usernameValidator(event.username)
                if(usernameResult.successful){
                    createUser(event.username)
                }
                state = state.copy(
                    showCreateUser = !usernameResult.successful,
                    createErrorMessage = usernameResult.errorMessage
                )
            }
            is UsersScreenEvent.UploadAppUsers -> {
                val unsyncedUsers = getUnsyncedDailyClasses()
                viewModelScope.launch(Dispatchers.IO) {
                    repository
                        .registerUsers(unsyncedUsers)
                        .collect{ collectAppUsers(it, true) }
                }
            }
        }
    }

    fun getUnsyncedDailyClasses() : List<AppUser> {
        return state.appUsers.filter { x -> x.modified }
    }

    private fun calculateAndModifyBalance(users: List<AppUser>): List<AppUser> {
        return users.map { user ->
            val userPayments = payments.filter { !it.cancelled && it.username == user.username }
            val userPlans = monthlyPlans.filter { !it.cancelled && it.username == user.username }
            val balance = userPayments.sumOf { it.amount } - userPlans.sumOf { it.planPrice }

            user.copy(
                balance = balance
            )
        }
    }

    private fun createUser(username: String) = viewModelScope.launch {
        val result = repository.addUserToDatabase(username = username)

        if (result is Resource.Success) {
            var users = state.appUsers.toMutableStateList()
            users.add(result.data!!)

            userFlow.emit(users)

        } else if (result is Resource.Error) {
            setUIEvent(
                UiEvent.ShowSnackbar(result.message!!)
            )
        }
    }

    private fun collectAppUsers(result: Resource<List<AppUser>>, fetchFromRemote: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                val users = calculateAndModifyBalance(result.data!!)
                if(state.appUsers != users){
                    userFlow.emit(result.data)
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

    fun getUnsyncedUsers() : List<AppUser> {
        if(isBusy)
            return emptyList()
        return state.appUsers.filter { x -> x.modified }
    }
}