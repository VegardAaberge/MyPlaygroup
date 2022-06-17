package com.myplaygroup.app.core.domain.validation.monthly_plans

import com.myplaygroup.app.core.domain.validation.ValidationResult

class KidNameValidator {
    operator fun invoke(data: String) : ValidationResult {
        if(data.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Kid cannot be blank"
            )
        }

        return ValidationResult(true)
    }
}