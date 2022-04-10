package com.myplaygroup.app.feature_login.domain.use_case

data class LoginUseCases (
    val authenticate: Authenticate,
    val resetPassword: ResetPassword
)
