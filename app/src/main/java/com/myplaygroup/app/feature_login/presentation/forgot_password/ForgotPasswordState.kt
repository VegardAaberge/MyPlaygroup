package com.myplaygroup.app.feature_login.presentation.forgot_password

data class ForgotPasswordState(
    val email: String = "",
    val code: String = "",
    val countDown: Int = -1,
    val shouldCheckCode: Boolean = false,
)