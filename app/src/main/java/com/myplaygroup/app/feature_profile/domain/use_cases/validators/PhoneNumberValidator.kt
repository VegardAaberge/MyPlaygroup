package com.myplaygroup.app.feature_profile.domain.use_cases.validators

import com.myplaygroup.app.feature_profile.domain.use_cases.ValidationResult

class PhoneNumberValidator {
    operator fun invoke(phoneNumber: String) : ValidationResult {
        if(phoneNumber.isEmpty()){
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