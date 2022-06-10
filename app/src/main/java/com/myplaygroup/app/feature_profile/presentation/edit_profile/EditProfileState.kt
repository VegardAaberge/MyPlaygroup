package com.myplaygroup.app.feature_profile.presentation.edit_profile

data class EditProfileState(
    val profileName: String = "",
    val profileNameError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
){
    fun isFilledIn() : Boolean {
        return profileName.isNotEmpty() && (phoneNumber.isNotEmpty())
    }
}