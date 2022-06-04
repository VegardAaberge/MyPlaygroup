package com.myplaygroup.app.feature_profile.presentation.edit_profile

import android.graphics.Bitmap

data class EditProfileState(
    val profileName: String = "",
    val profileNameError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val email: String = "",
    val emailError: String? = null,
){
    fun isFilledIn() : Boolean {
        return profileName.isNotEmpty() && (phoneNumber.isNotEmpty() || email.isNotEmpty())
    }
}