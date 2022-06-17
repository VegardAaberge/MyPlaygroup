package com.myplaygroup.app.core.domain.validation.user

import com.myplaygroup.app.core.domain.validation.ValidationResult

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