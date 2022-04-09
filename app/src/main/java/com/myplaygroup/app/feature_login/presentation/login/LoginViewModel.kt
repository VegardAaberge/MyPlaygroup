package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.feature_login.domain.use_case.LoginUseCases
import com.myplaygroup.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : ViewModel() {

    private val _user = mutableStateOf<String>("")
    val user: State<String> = _user

    private val _password = mutableStateOf<String>("")
    val password: State<String> = _password

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var isLoading = false

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.EnteredUsername -> {
                _user.value = event.user
            }
            is LoginEvent.EnteredPassword -> {
                _password.value = event.password
            }
            is LoginEvent.LoginTapped -> {
                viewModelScope.launch {
                    isLoading = true
                    val response = loginUseCases.authenticate(_user.value, _password.value)

                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(response.data!!)
                    )
                    isLoading = false
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}