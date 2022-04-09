package com.myplaygroup.app.feature_login.presentation.login

import androidx.lifecycle.ViewModel
import com.myplaygroup.app.feature_login.domain.use_case.LoginUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases
) : ViewModel() {
    val loginMessage = "Login Screen from\n LoginViewModel"
}