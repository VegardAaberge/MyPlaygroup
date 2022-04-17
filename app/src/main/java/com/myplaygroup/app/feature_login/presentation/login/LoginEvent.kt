package com.myplaygroup.app.feature_login.presentation.login

sealed class LoginEvent{
    data class EnteredUsername(val username: String) : LoginEvent()
    data class EnteredPassword(val password: String) : LoginEvent()
    object LoginTapped : LoginEvent()
}
