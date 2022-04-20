package com.myplaygroup.app.feature_login.presentation.create_profile

import android.graphics.Bitmap

sealed class CreateProfileScreenEvent {
    object SaveProfile : CreateProfileScreenEvent()
    object TakePicture : CreateProfileScreenEvent()
    data class TakePictureDone(val bitmap: Bitmap) : CreateProfileScreenEvent()
    data class EnteredProfileName(val profileName: String) : CreateProfileScreenEvent()
    data class EnteredPassword(val password: String) : CreateProfileScreenEvent()
    data class EnteredRepeatedPassword(val repeatedPassword: String) : CreateProfileScreenEvent()
    data class EnteredPhoneNumber(val phoneNumber: String) : CreateProfileScreenEvent()
    data class EnteredEmail(val email: String) : CreateProfileScreenEvent()
}