package com.myplaygroup.app.feature_profile.domain.interactors

import com.myplaygroup.app.core.domain.validation.user.*
import javax.inject.Inject

class ProfileValidationInteractors @Inject constructor() {
    val profileNameValidator: ProfileNameValidator = ProfileNameValidator()
    val phoneNumberValidator: PhoneNumberValidator = PhoneNumberValidator()
    val passwordValidator: PasswordValidator = PasswordValidator()
    val repeatedPasswordValidator: RepeatedPasswordValidator = RepeatedPasswordValidator()
    val profileBitmapValidator: ProfileBitmapValidator = ProfileBitmapValidator()
}