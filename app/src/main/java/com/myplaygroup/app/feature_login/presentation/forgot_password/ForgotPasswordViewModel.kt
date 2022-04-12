package com.myplaygroup.app.feature_login.presentation.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_login.domain.model.User
import com.myplaygroup.app.feature_login.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: LoginRepository
) : BaseViewModel() {

    var state by mutableStateOf(ForgotPasswordState())

    fun onEvent(event: ForgotPasswordEvent)
    {
        when(event){
            is ForgotPasswordEvent.EnteredEmail -> {
                state = state.copy(email = event.email)
            }
            is ForgotPasswordEvent.EnteredCode -> {
                state = state.copy(code = event.code)
            }
            is ForgotPasswordEvent.ActionButtonTapped -> {
                viewModelScope.launch {
                    if(state.shouldCheckCode){
                        repository
                            .checkVerificationCode(state.code)
                            .collect { collectCodeRequest(it) }
                    }else{
                        repository
                            .sendEmailRequestForm(state.email)
                            .collect { collectEmailRequest(it) }
                    }
                }
            }
        }
    }

    private suspend fun collectEmailRequest(result: Resource<String>) {
        when(result){
            is Resource.Success -> {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar("Sent, please check your email")
                )
                state = state.copy(
                    shouldCheckCode = true,
                    countDown = 60,
                )

                viewModelScope.launch (Dispatchers.Default) {
                    while (state.countDown >= 0){
                        delay(1000)
                        state = state.copy(countDown = state.countDown - 1)
                    }
                    state = state.copy(
                        shouldCheckCode = false,
                        code = ""
                    )
                }
            }
            is Resource.Error -> {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar("Failed to send")
                )
            }
            is Resource.Loading -> {
                _isBusy.value = result.isLoading
            }
        }
    }

    private suspend fun collectCodeRequest(result: Resource<String>) {
        when(result){
            is Resource.Success -> {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar("Correct Code")
                )

                state = state.copy(
                    shouldCheckCode = false,
                    countDown = -1,
                    email = ""
                )
            }
            is Resource.Error -> {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar("Failed to send")
                )

                state = state.copy(
                    shouldCheckCode = false,
                    countDown = -1,
                )
            }
            is Resource.Loading -> {
                _isBusy.value = result.isLoading
            }
        }
    }
}