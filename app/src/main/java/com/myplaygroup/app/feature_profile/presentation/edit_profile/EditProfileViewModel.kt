package com.myplaygroup.app.feature_profile.presentation.edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
import com.myplaygroup.app.feature_profile.domain.interactors.ProfileValidationInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userSettingsManager: UserSettingsManager,
    private val profileUseCases: ProfileValidationInteractors
) : BaseViewModel() {

    var state by mutableStateOf(EditProfileState())

    init {
        viewModelScope.launch {
            userSettingsManager.getFlow().collect {
                state = state.copy(
                    profileName = it.profileName,
                    phoneNumber = it.phoneNumber,
                    email = it.email
                )
            }
        }
    }

    fun onEvent(event: EditProfileScreenEvent){
        when(event){
            is EditProfileScreenEvent.EnteredProfileName -> {
                state = state.copy(profileName = event.profileName)
            }
            is EditProfileScreenEvent.EnteredPhoneNumber -> {
                state = state.copy(phoneNumber = event.phoneNumber)
            }
            is EditProfileScreenEvent.EnteredEmail -> {
                state = state.copy(email = event.email)
            }
            is EditProfileScreenEvent.SaveProfile -> {
                submitData()
            }
        }
    }

    private fun submitData() = viewModelScope.launch{

        val profileNameResult = profileUseCases.profileNameValidator(state.profileName)
        val emailResult = profileUseCases.emailValidator(state.email)
        val phoneNumberResult = profileUseCases.phoneNumberValidator(state.phoneNumber)

        val hasError = listOf(
            profileNameResult, emailResult, phoneNumberResult
        ).any { !it.successful }

        state = state.copy(
            profileNameError = profileNameResult.errorMessage,
            emailError = emailResult.errorMessage,
            phoneNumberError = phoneNumberResult.errorMessage,
        )

        if(!hasError){
            val username = userSettingsManager.getFlow { x -> x.map { u -> u.username } }.first()

            launch(Dispatchers.IO) {
                profileRepository.editProfile(
                    username = username,
                    profileName = state.profileName,
                    phoneNumber = state.phoneNumber,
                    email = state.email,
                ).collect { collectCreateProfile(it) }
            }
        }
    }

    private fun collectCreateProfile(result: Resource<Unit>){
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