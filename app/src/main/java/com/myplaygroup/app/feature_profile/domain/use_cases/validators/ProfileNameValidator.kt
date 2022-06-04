package com.myplaygroup.app.feature_profile.domain.use_cases.validators

import com.myplaygroup.app.feature_profile.domain.use_cases.ValidationResult

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