package com.myplaygroup.app.feature_profile.presentation.edit_profile

sealed class EditProfileScreenEvent {
    object SaveProfile : EditProfileScreenEvent()
    data class EnteredProfileName(val profileName: String) : EditProfileScreenEvent()
    data class EnteredPhoneNumber(val phoneNumber: String) : EditProfileScreenEvent()
}