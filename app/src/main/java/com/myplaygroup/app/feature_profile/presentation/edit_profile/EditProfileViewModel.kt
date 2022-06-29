package com.myplaygroup.app.feature_profile.presentation.edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.settings.UserRole
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_profile.domain.interactors.ProfileValidationInteractors
import com.myplaygroup.app.feature_profile.domain.model.EditProfileType
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
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
                    isAdmin = it.userRole == UserRole.ADMIN.name,
                    profileName = it.profileName,
                    phoneNumber = it.phoneNumber,
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
            is EditProfileScreenEvent.EnteredPassword -> {
                state = state.copy(phoneNumber = event.password)
            }
            is EditProfileScreenEvent.EnteredRepeatedPassword -> {
                state = state.copy(phoneNumber = event.repeatedPassword)
            }
            is EditProfileScreenEvent.DropdownChanged -> {
                state = state.copy(editProfileType = event.profileType)
            }
            is EditProfileScreenEvent.SaveProfile -> {
                submitData()
            }
        }
    }

    private fun submitData() = viewModelScope.launch{

        val hasError = verifyData()

        if(!hasError){
            val username = userSettingsManager.getFlow { x -> x.map { u -> u.username } }.first()

            launch(Dispatchers.IO) {
                profileRepository.editProfile(
                    username = username,
                    profileName = state.profileName,
                    phoneNumber = state.phoneNumber,
                    newPassword = state.password,
                    editProfileType = state.editProfileType
                ).collect { collectCreateProfile(it) }
            }
        }
    }

    private fun verifyData() : Boolean {

        return when(state.editProfileType){
            EditProfileType.PROFILE_NAME -> {
                val profileNameResult = profileUseCases.profileNameValidator(state.profileName)
                state = state.copy(
                    profileNameError = profileNameResult.errorMessage,
                )
                !profileNameResult.successful
            }
            EditProfileType.PHONE_NUMBER -> {
                val phoneNumberResult = profileUseCases.phoneNumberValidator(state.phoneNumber)
                state = state.copy(
                    phoneNumberError = phoneNumberResult.errorMessage,
                )
                !phoneNumberResult.successful
            }
            EditProfileType.PASSWORD -> {
                val passwordResult = profileUseCases.passwordValidator(state.password)
                val repeatedPasswordResult = profileUseCases.repeatedPasswordValidator(
                    state.password, state.repeatedPassword
                )
                state = state.copy(
                    passwordError = passwordResult.errorMessage,
                    repeatedPasswordError = repeatedPasswordResult.errorMessage,
                )
                listOf(
                    passwordResult, repeatedPasswordResult
                ).any { !it.successful }
            }
            else -> {
                setUIEvent(
                    UiEvent.ShowSnackbar("No Profile Type selected")
                )
                true
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