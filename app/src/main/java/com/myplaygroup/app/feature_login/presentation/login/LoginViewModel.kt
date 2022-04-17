package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.destinations.LoginScreenDestination
import com.myplaygroup.app.destinations.MainScrenDestination
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val basicAuthInterceptor: BasicAuthInterceptor
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

    private suspend fun collectAuthenticateResponse(result: Resource<String>) {
        when(result){
            is Resource.Success -> {
                _eventFlow.emit(
                    UiEvent.PopAndNavigateTo(
                        popRoute = LoginScreenDestination.route,
                        destination = MainScrenDestination()
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