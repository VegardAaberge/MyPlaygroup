package com.myplaygroup.app.core.domain.validation

class ProfileNameValidator {
    operator fun invoke(profileName: String) : ValidationResult {
        if(profileName.isEmpty()){
            return ValidationResult(
                successful = false,
                errorMessage = "Profile name cannot be blank"
            )
        }

        return ValidationResult(true)
    }
}