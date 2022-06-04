package com.myplaygroup.app.feature_profile.domain.use_cases.validators

import com.myplaygroup.app.feature_profile.domain.use_cases.ValidationResult

class RepeatedPasswordValidator {
    operator fun invoke(password: String, repeatedPassword: String) : ValidationResult {
        if(password != repeatedPassword){
            return ValidationResult(
                successful = false,
                errorMessage = "The password don't match"
            )
        }
        return ValidationResult(true)
    }
}