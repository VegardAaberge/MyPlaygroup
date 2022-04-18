package com.myplaygroup.app.feature_login.presentation.create_profile

data class CreateProfileState(
    val profileName: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val phoneNumber: String = "",
    val email: String = ""
)