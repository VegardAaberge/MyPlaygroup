package com.myplaygroup.app.feature_profile.domain.use_cases

import com.myplaygroup.app.feature_profile.domain.use_cases.validators.*

data class ProfileUseCases (
    val profileNameValidator: ProfileNameValidator = ProfileNameValidator(),
    val phoneNumberValidator: PhoneNumberValidator = PhoneNumberValidator(),
    val passwordValidator: PasswordValidator = PasswordValidator(),
    val repeatedPasswordValidator: RepeatedPasswordValidator = RepeatedPasswordValidator(),
    val email: EmailValidator = EmailValidator(),
)