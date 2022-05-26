package com.myplaygroup.app.feature_profile.presentation.edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.data.pref.UserSettings
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    dataStore: DataStore<UserSettings>
) : BaseViewModel() {

    var state by mutableStateOf(EditProfileState())

    init {
        viewModelScope.launch {
            dataStore.data.collect {
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