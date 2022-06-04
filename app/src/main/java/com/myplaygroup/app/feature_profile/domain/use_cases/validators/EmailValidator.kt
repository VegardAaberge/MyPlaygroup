package com.myplaygroup.app.feature_profile.domain.use_cases.validators

import android.util.Patterns
import com.myplaygroup.app.feature_profile.domain.use_cases.ValidationResult

class EmailValidator {
    operator fun invoke(email: String) : ValidationResult {
        if(email.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "The email can't be blank"
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                successful = false,
                errorMessage = "That's not a valid email"
            )
        }
        return ValidationResult(true)
    }
}