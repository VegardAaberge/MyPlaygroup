package com.myplaygroup.app.feature_login.presentation.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_login.domain.use_case.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : BaseViewModel() {

    var state by mutableStateOf(ForgotPasswordState())

    fun onEvent(event: ForgotPasswordEvent)
    {
        when(event){
            is ForgotPasswordEvent.EnteredEmail -> {
                state = state.copy(email = event.email)
            }
            is ForgotPasswordEvent.ResetPassword -> {
                viewModelScope.launch {
                    _isBusy.value = true
                    val response = loginUseCases.resetPassword(state.email)

                    when(response){
                        is Resource.Success -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar("Sent, please check your email")
                            )
                        }
                        is Resource.Error -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackbar("Failed to send")
                            )
                        }
                    }
                    _isBusy.value = false
                }
            }
        }
    }
}