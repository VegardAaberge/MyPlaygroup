package com.myplaygroup.app.core.domain.validation.user

import com.myplaygroup.app.R
import com.myplaygroup.app.core.domain.validation.ValidationResult

class ProfileNameValidator(val context: android.content.Context) {
    operator fun invoke(profileName: String) : ValidationResult {
        if(profileName.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validation_profile_name_cannot_be_blank)
            )
        }

        return ValidationResult(true)
    }
}