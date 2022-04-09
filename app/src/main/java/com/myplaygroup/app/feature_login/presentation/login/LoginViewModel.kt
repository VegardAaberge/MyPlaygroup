package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.feature_login.domain.use_case.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
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
                    loginUseCases.authenticate(_user.value, _password.value)
                }
            }
        }
    }
}