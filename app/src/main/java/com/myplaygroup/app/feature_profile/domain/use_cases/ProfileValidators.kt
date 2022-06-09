package com.myplaygroup.app.feature_profile.domain.use_cases

import com.myplaygroup.app.feature_profile.domain.use_cases.validators.*
import javax.inject.Inject

class ProfileValidators @Inject constructor() {
    val profileNameValidator: ProfileNameValidator = ProfileNameValidator()
    val phoneNumberValidator: PhoneNumberValidator = PhoneNumberValidator()
    val passwordValidator: PasswordValidator = PasswordValidator()
    val repeatedPasswordValidator: RepeatedPasswordValidator = RepeatedPasswordValidator()
    val emailValidator: EmailValidator = EmailValidator()
    val profileBitmapValidator: ProfileBitmapValidator = ProfileBitmapValidator()
}