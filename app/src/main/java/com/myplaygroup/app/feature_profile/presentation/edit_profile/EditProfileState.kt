package com.myplaygroup.app.feature_profile.presentation.edit_profile

import com.myplaygroup.app.feature_profile.domain.model.EditProfileType

data class EditProfileState(
    val profileName: String = "",
    val profileNameError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val isAdmin: Boolean = false,
    val editProfileType: EditProfileType = EditProfileType.NONE
){
    fun isFilledIn() : Boolean {
        return profileName.isNotEmpty() && (phoneNumber.isNotEmpty())
    }
}