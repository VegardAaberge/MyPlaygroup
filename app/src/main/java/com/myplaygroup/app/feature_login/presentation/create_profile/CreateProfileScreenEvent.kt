package com.myplaygroup.app.feature_login.presentation.create_profile

sealed class CreateProfileScreenEvent {
    data class EnteredProfileName(val profileName: String) : CreateProfileScreenEvent()
    data class EnteredPassword(val password: String) : CreateProfileScreenEvent()
    data class EnteredRepeatedPassword(val repeatedPassword: String) : CreateProfileScreenEvent()
}