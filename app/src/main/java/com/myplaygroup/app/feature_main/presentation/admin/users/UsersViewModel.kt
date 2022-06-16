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
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UsersRepository,
    private val validators: MainValidationInteractors
) : BaseViewModel() {

    var state by mutableStateOf(UsersState())

    fun init(userFlow: MutableStateFlow<List<AppUser>>) {
        userFlow.onEach { appUsers ->
            state = state.copy(
                appUsers = appUsers
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

    private fun collectAppUsers(result: Resource<List<AppUser>>, fetchFromRemote: Boolean) = viewModelScope.launch(Dispatchers.Main) {
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