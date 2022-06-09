package com.myplaygroup.app.core.domain.validation

import androidx.core.util.PatternsCompat

class EmailValidator(

) {
    operator fun invoke(email: String) : ValidationResult {
        if(email.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "The email can't be blank"
            )
        }
        if(!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                successful = false,
                errorMessage = "That's not a valid email"
            )
        }
        return ValidationResult(true)
    }
}