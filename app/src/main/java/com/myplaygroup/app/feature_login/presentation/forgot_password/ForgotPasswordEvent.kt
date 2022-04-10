package com.myplaygroup.app.feature_login.presentation.forgot_password

import com.myplaygroup.app.feature_login.presentation.login.LoginEvent

sealed class ForgotPasswordEvent {
    data class EnteredEmail(val email: String) : ForgotPasswordEvent()
    object ResetPassword : ForgotPasswordEvent()
}