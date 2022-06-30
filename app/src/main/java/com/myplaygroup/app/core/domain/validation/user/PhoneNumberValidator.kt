package com.myplaygroup.app.core.domain.validation.user

import com.myplaygroup.app.R
import com.myplaygroup.app.core.domain.validation.ValidationResult

class PhoneNumberValidator(val context: android.content.Context) {
    operator fun invoke(phoneNumber: String) : ValidationResult {
        if(phoneNumber.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validation_phone_number_canot_be_blank)
            )
        }
        if(phoneNumber.any{ x -> !x.isDigit() }){
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validation_phone_number_contain_digits)
            )
        }
        if(phoneNumber.length != 11){
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validation_phone_number_contain_11_digits)
            )
        }

        return ValidationResult(true)
    }
}