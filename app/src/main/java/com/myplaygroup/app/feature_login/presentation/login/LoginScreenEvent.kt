package com.myplaygroup.app.feature_login.presentation.login

sealed class LoginScreenEvent{
    data class EnteredUsername(val username: String) : LoginScreenEvent()
    data class EnteredPassword(val password: String) : LoginScreenEvent()
    object LoginTapped : LoginScreenEvent()
}
