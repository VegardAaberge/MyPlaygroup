package com.myplaygroup.app.feature_main.presentation.admin.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    repository: UsersRepository
) : BaseViewModel() {

    var state by mutableStateOf(UsersState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .getAllUsers(true)
                .collect{ collectAppUsers(it) }
        }
    }

    fun onEvent(event: UsersScreenEvent) {
        when (event) {
            is UsersScreenEvent.CreateUserDialog -> {
                state = state.copy(showCreateUser = event.show)
            }
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
}