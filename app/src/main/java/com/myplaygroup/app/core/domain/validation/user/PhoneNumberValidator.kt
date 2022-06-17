package com.myplaygroup.app.core.domain.validation.user

import com.myplaygroup.app.core.domain.validation.ValidationResult

class PhoneNumberValidator {
    operator fun invoke(phoneNumber: String) : ValidationResult {
        if(phoneNumber.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "The phone number cannpt be blank"
            )
        }
        if(phoneNumber.any{ x -> !x.isDigit() }){
            return ValidationResult(
                successful = false,
                errorMessage = "The phone should only consist of digits"
            )
        }
        if(phoneNumber.length != 11){
            return ValidationResult(
                successful = false,
                errorMessage = "The phone number need to contain 11 digits"
            )
        }

        return ValidationResult(true)
    }
}