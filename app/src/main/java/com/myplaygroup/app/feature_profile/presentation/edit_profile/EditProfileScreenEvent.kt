package com.myplaygroup.app.feature_profile.presentation.edit_profile

import com.myplaygroup.app.feature_profile.domain.model.EditProfileType

sealed class EditProfileScreenEvent {
    object SaveProfile : EditProfileScreenEvent()
    data class EnteredProfileName(val profileName: String) : EditProfileScreenEvent()
    data class EnteredPhoneNumber(val phoneNumber: String) : EditProfileScreenEvent()
    data class EnteredPassword(val password: String) : EditProfileScreenEvent()
    data class EnteredRepeatedPassword(val repeatedPassword: String) : EditProfileScreenEvent()
    data class DropdownChanged(val profileType: EditProfileType) : EditProfileScreenEvent()
}