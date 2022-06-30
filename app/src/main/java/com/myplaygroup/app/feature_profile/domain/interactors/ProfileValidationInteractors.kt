package com.myplaygroup.app.feature_profile.domain.interactors

import android.content.Context
import com.myplaygroup.app.core.domain.validation.user.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProfileValidationInteractors @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val profileNameValidator: ProfileNameValidator = ProfileNameValidator(context)
    val phoneNumberValidator: PhoneNumberValidator = PhoneNumberValidator(context)
    val passwordValidator: PasswordValidator = PasswordValidator(context)
    val repeatedPasswordValidator: RepeatedPasswordValidator = RepeatedPasswordValidator(context)
    val profileBitmapValidator: ProfileBitmapValidator = ProfileBitmapValidator(context)
}