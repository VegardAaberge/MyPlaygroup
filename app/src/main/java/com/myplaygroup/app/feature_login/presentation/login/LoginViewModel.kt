package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.feature_login.domain.use_case.LoginUseCases
import com.myplaygroup.app.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : BaseViewModel() {

    var state by mutableStateOf(LoginState())

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.EnteredUsername -> {
                state = state.copy(user = event.user)
            }
            is LoginEvent.EnteredPassword -> {
                state = state.copy(password = event.password)
            }
            is LoginEvent.LoginTapped -> {
                viewModelScope.launch {
                    _isBusy.value = true
                    val response = loginUseCases.authenticate(state.user, state.password)

                    when(response){
                        is Resource.Success -> {
                            _isBusy.value = false
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar("Success")
                            )
                        }
                        is Resource.Error -> {
                            _isBusy.value = false
                            state = state.copy(password = "")

                            _eventFlow.emit(
                                UiEvent.ShowSnackbar("Fail")
                            )
                        }
                    }
                }
            }
        }
    }
}