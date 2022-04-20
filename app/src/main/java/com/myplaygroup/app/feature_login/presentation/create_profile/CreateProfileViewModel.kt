package com.myplaygroup.app.feature_login.presentation.create_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val imageRepository : ImageRepository
) : BaseViewModel() {

    var state by mutableStateOf(CreateProfileState())

    fun onEvent(event: CreateProfileScreenEvent){
        when(event){
            is CreateProfileScreenEvent.EnteredProfileName -> {
                state = state.copy(profileName = event.profileName)
            }
            is CreateProfileScreenEvent.EnteredPhoneNumber -> {
                state = state.copy(phoneNumber = event.phoneNumber)
            }
            is CreateProfileScreenEvent.EnteredEmail -> {
                state = state.copy(email = event.email)
            }
            is CreateProfileScreenEvent.EnteredPassword -> {
                state = state.copy(password = event.password)
            }
            is CreateProfileScreenEvent.EnteredRepeatedPassword -> {
                state = state.copy(repeatedPassword = event.repeatedPassword)
            }
            is CreateProfileScreenEvent.TakePicture -> {
                state = state.copy(takePictureMode = true)
            }
            is CreateProfileScreenEvent.TakePictureDone -> {
                state = state.copy(
                    takePictureMode = false,
                    profileBitmap = event.bitmap
                )
            }
            is CreateProfileScreenEvent.SaveProfile -> {
                viewModelScope.launch {
                    state.profileBitmap?.let {
                        imageRepository.saveProfileImage(it)
                    }
                }
            }
        }
    }
}