package com.myplaygroup.app.feature_login.presentation.create_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.feature_login.presentation.forgot_password.ForgotPasswordState
import javax.inject.Inject

class CreateProfileViewModel @Inject constructor(

) : BaseViewModel() {

    var state by mutableStateOf(CreateProfileState())

    fun onEvent(event: CreateProfileScreenEvent){
        when(event){
            is CreateProfileScreenEvent.EnteredProfileName -> {
                state = state.copy(profileName = event.profileName)
            }
            is CreateProfileScreenEvent.EnteredPassword -> {
                state = state.copy(password = event.password)
            }
            is CreateProfileScreenEvent.EnteredRepeatedPassword -> {
                state = state.copy(repatedPassword = event.repeatedPassword)
            }
        }
    }
}