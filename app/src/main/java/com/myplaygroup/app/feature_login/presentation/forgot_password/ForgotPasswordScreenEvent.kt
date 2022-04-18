package com.myplaygroup.app.feature_login.presentation.forgot_password

sealed class ForgotPasswordScreenEvent {
    data class EnteredEmail(val email: String) : ForgotPasswordScreenEvent()
    data class EnteredCode(val code: String) : ForgotPasswordScreenEvent()
    object ActionButtonTapped : ForgotPasswordScreenEvent()
}