package com.myplaygroup.app.feature_main.presentation.admin.users

import android.os.SystemClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.interactors.MainValidationInteractors
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UsersRepository,
    private val validators: MainValidationInteractors
) : BaseViewModel() {

    private var lastRefresh: Long = 0

    var state by mutableStateOf(UsersState())

    init {
        fetchData(true)
    }

    fun onEvent(event: UsersScreenEvent) {
        when (event) {
            is UsersScreenEvent.RefreshData -> {
                fetchData(false)
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
                        .collect{ collectAppUsers(it) }
                }
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
                .getAllUsers(fetchFromRemote)
                .collect{ collectAppUsers(it) }
        }
    }

    fun getUnsyncedDailyClasses() : List<AppUser> {
        return state.appUsers.filter { x -> x.modified }
    }

    private fun createUser(username: String) = viewModelScope.launch {
        val result = repository.addUserToDatabase(username = username)

        if (result is Resource.Success) {
            val users = state.appUsers.toMutableStateList()
            users.add(result.data!!)

            state = state.copy(
                appUsers = users
            )
        } else if (result is Resource.Error) {
            setUIEvent(
                UiEvent.ShowSnackbar(result.message!!)
            )
        }
    }

    private fun collectAppUsers(result: Resource<List<AppUser>>) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                state = state.copy(
                    appUsers = result.data!!
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

    fun getUnsyncedUsers() : List<AppUser> {
        return state.appUsers.filter { x -> x.modified }
    }
}