package com.myplaygroup.app.feature_profile.presentation.create_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
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

                    if(!state.isFilledIn()){
                        setUIEvent(
                            UiEvent.ShowSnackbar("Please fill out all the fields")
                        )
                    }

                    if(state.password != state.repeatedPassword){
                        setUIEvent(
                            UiEvent.ShowSnackbar("The passwords do not match")
                        )
                    }

                    state.profileBitmap?.let {
                        val response = imageRepository.storeProfileImage(it)
                        when(response){
                            is Resource.Success -> {
                                setUIEvent(
                                    UiEvent.ShowSnackbar("uploaded profile image")
                                )
                            }
                            is Resource.Error -> {
                                setUIEvent(
                                    UiEvent.ShowSnackbar(response.message!!)
                                )
                            }
                        }
                    }
                    val uri = imageRepository.getProfileImage().data

                    launch(Dispatchers.IO) {
                        profileRepository.createProfile(
                            profileUri = uri,
                            profileName = state.profileName,
                            phoneNumber = state.phoneNumber,
                            email = state.email,
                            newPassword = state.password
                        ).collect { collectCreateProfile(it) }
                    }
                }
            }
        }
    }

    private suspend fun collectCreateProfile(result: Resource<Unit>){
        when (result) {
            is Resource.Success -> {
                setUIEvent(
                    UiEvent.PopPage
                )
            }
            is Resource.Error -> {
                setUIEvent(
                    UiEvent.ShowSnackbar("Create Profile: " + result.message!!)
                )
            }
            is Resource.Loading -> {
                isBusy(result.isLoading)
            }
        }
    }
}