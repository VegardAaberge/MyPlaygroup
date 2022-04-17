package com.myplaygroup.app.feature_login.presentation.login

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.util.Constants.KEY_PASSWORD
import com.myplaygroup.app.core.util.Constants.KEY_USERNAME
import com.myplaygroup.app.destinations.LoginScreenDestination
import com.myplaygroup.app.destinations.MainScreenDestination
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val sharedPref: SharedPreferences
) : BaseViewModel() {

    var state by mutableStateOf(LoginState())

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.EnteredUsername -> {
                state = state.copy(username = event.username)
            }
            is LoginEvent.EnteredPassword -> {
                state = state.copy(password = event.password)
            }
            is LoginEvent.LoginTapped -> {
                authenticateAPI()
                storeAuthentication()
                viewModelScope.launch {
                    repository
                        .authenticate(state.username, state.password)
                        .collect { collectAuthenticateResponse(it) }
                }
            }
        }
    }

    private fun authenticateAPI(){
        basicAuthInterceptor.username = state.username
        basicAuthInterceptor.password = state.password
    }

    private fun storeAuthentication(){
        sharedPref.edit {
            putString(KEY_USERNAME, state.username)
            putString(KEY_PASSWORD, state.password)
            apply()
        }
    }

    private suspend fun collectAuthenticateResponse(result: Resource<String>) {
        when(result){
            is Resource.Success -> {
                _eventFlow.emit(
                    UiEvent.PopAndNavigateTo(
                        popRoute = LoginScreenDestination.route,
                        destination = MainScreenDestination
                    )
                )
            }
            is Resource.Error -> {
                state = state.copy(password = "")

                _eventFlow.emit(
                    UiEvent.ShowSnackbar("Fail")
                )
            }
            is Resource.Loading -> {
                _isBusy.value = result.isLoading
            }
        }
    }
}