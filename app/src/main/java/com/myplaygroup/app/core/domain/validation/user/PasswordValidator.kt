package com.myplaygroup.app.core.domain.validation.user

import com.myplaygroup.app.core.domain.validation.ValidationResult

class PasswordValidator {
    operator fun invoke(password: String) : ValidationResult {
        if(password.length < 8){
            return ValidationResult(
                successful = false,
                errorMessage = "The password needs to consist of at least 8 characters"
            )
        }
        val constainsLetterAndDigits = password.any { it.isDigit() }
                && password.any{ it.isLetter() }
                && password.any { it.isUpperCase() }
                && password.any { it.isLowerCase() }


        if(!constainsLetterAndDigits){
            return ValidationResult(
                successful = false,
                errorMessage = "The password need to contain one letter, one digit and one upper and lower case letter"
            )
        }
        return ValidationResult(true)
    }
}