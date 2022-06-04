package com.myplaygroup.app.feature_profile.presentation.create_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
import com.myplaygroup.app.feature_profile.domain.use_cases.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val imageRepository : ImageRepository,
    private val userSettingsManager: UserSettingsManager,
    private val profileUseCases: ProfileUseCases
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
                    profileBitmap = event.bitmap,
                    profileBitmapError = null
                )
            }
            is CreateProfileScreenEvent.SaveProfile -> {
                submitData()
            }
        }
    }

    private fun submitData() = viewModelScope.launch{

        val profileBitmapResult = profileUseCases.profileBitmapValidator(state.profileBitmap)
        val profileNameResult = profileUseCases.profileNameValidator(state.profileName)
        val emailResult = profileUseCases.emailValidator(state.email)
        val phoneNumberResult = profileUseCases.phoneNumberValidator(state.phoneNumber)
        val passwordResult = profileUseCases.passwordValidator(state.password)
        val repeatedPasswordResult = profileUseCases.repeatedPasswordValidator(
            state.password, state.repeatedPassword
        )

        val hasError = listOf(
            profileBitmapResult, profileNameResult, emailResult, phoneNumberResult, passwordResult, repeatedPasswordResult
        ).any { !it.successful }

        state = state.copy(
            profileBitmapError = profileBitmapResult.errorMessage,
            profileNameError = profileNameResult.errorMessage,
            emailError = emailResult.errorMessage,
            phoneNumberError = phoneNumberResult.errorMessage,
            passwordError = passwordResult.errorMessage,
            repeatedPasswordError = repeatedPasswordResult.errorMessage,
        )

        if(!hasError){
            val username = userSettingsManager.getFlow { x -> x.map { u -> u.username } }.first()

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
                    else -> { }
                }
            }

            launch(Dispatchers.IO) {
                profileRepository.createProfile(
                    username = username,
                    profileName = state.profileName,
                    phoneNumber = state.phoneNumber,
                    email = state.email,
                    newPassword = state.password
                ).collect { collectCreateProfile(it) }
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
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
            is Resource.Loading -> {
                isBusy(result.isLoading)
            }
        }
    }
}