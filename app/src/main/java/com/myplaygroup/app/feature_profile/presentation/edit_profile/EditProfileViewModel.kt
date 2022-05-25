package com.myplaygroup.app.feature_profile.presentation.edit_profile

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel() {

    var state by mutableStateOf(EditProfileState())

    init {
        val profileName = sharedPreferences.getString(Constants.KEY_PROFILE_NAME, "") ?: ""
        val phoneNumber = sharedPreferences.getString(Constants.KEY_PHONE_NUMBER, "") ?: ""
        val email = sharedPreferences.getString(Constants.KEY_EMAIL, "") ?: ""

        state = state.copy(
            profileName = profileName,
            phoneNumber = phoneNumber,
            email = email
        )
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

                viewModelScope.launch {

                    if(!state.isFilledIn()){
                        setUIEvent(
                            UiEvent.ShowSnackbar("Please fill out all the fields")
                        )
                    }

                    launch(Dispatchers.IO) {
                        profileRepository.editProfile(
                            profileName = state.profileName,
                            phoneNumber = state.phoneNumber,
                            email = state.email,
                        ).collect { collectCreateProfile(it) }
                    }
                }
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