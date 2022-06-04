package com.myplaygroup.app.feature_login.presentation.forgot_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_login.data.responses.SendResetPasswordResponse
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

    var token = ""

    fun onEvent(event: ForgotPasswordScreenEvent)
    {
        when(event){
            is ForgotPasswordScreenEvent.EnteredEmail -> {
                state = state.copy(email = event.email)
            }
            is ForgotPasswordScreenEvent.EnteredCode -> {
                state = state.copy(code = event.code)
            }
            is ForgotPasswordScreenEvent.ActionButtonTapped -> {
                viewModelScope.launch {
                    if(state.shouldCheckCode){
                        repository
                            .checkVerificationCode(state.code, token)
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

    private suspend fun collectEmailRequest(result: Resource<SendResetPasswordResponse>) {
        when(result){
            is Resource.Success -> {
                setUIEvent(
                    UiEvent.ShowSnackbar("Success")
                )
                state = state.copy(
                    shouldCheckCode = true,
                    countDown = 60,
                )
                token = result.data!!.token

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
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
            is Resource.Loading -> {
                isBusy(result.isLoading)
            }
        }
    }

    private suspend fun collectCodeRequest(result: Resource<Unit>) {
        when(result){
            is Resource.Success -> {
                setUIEvent(
                    UiEvent.ShowSnackbar("Successful")
                )

                state = state.copy(
                    shouldCheckCode = false,
                    countDown = -1,
                    email = ""
                )
            }
            is Resource.Error -> {
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )

                state = state.copy(
                    shouldCheckCode = false,
                    countDown = -1,
                )
            }
            is Resource.Loading -> {
                isBusy(result.isLoading)
            }
        }
    }
}