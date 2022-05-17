package com.myplaygroup.app.feature_profile.presentation.edit_profile

import android.graphics.Bitmap

data class EditProfileState(
    val profileName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
){
    fun isFilledIn() : Boolean {
        return profileName.isNotEmpty() && (phoneNumber.isNotEmpty() || email.isNotEmpty())
    }
}