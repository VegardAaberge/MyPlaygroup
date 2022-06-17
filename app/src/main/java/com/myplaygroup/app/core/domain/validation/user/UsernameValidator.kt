package com.myplaygroup.app.core.domain.validation.user

import com.myplaygroup.app.core.domain.validation.ValidationResult

class UsernameValidator {
    operator fun invoke(username: String) : ValidationResult {
        if(username.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Can't be blank"
            )
        }
        if(username.any { x -> x.isUpperCase() }){
            return ValidationResult(
                successful = false,
                errorMessage = "No uppercase letters"
            )
        }
        if(username.any { x -> !x.isLetter() }){
            return ValidationResult(
                successful = false,
                errorMessage = "Can only have letters"
            )
        }

        return ValidationResult(true)
    }
}