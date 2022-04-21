package com.myplaygroup.app.feature_login.presentation.create_profile

import android.graphics.Bitmap

data class CreateProfileState(
    val profileName: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val phoneNumber: String = "",
    val email: String = "",
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