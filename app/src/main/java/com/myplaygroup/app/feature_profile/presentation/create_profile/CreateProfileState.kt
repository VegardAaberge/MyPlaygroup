package com.myplaygroup.app.feature_profile.presentation.create_profile

import android.graphics.Bitmap

data class CreateProfileState(
    val profileName: String = "",
    val profileNameError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val takePictureMode: Boolean = false,
    val profileBitmap: Bitmap? = null
){
    fun isFilledIn() : Boolean {
        return profileName.isNotEmpty()
                && (phoneNumber.isNotEmpty() || email.isNotEmpty())
                && password.isNotEmpty()
                && repeatedPassword.isNotEmpty()
                && profileBitmap != null
    }
}