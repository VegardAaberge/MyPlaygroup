package com.myplaygroup.app.core.domain.validation.monthly_plans

import com.myplaygroup.app.core.domain.validation.ValidationResult

class PlanNameValidator {
    operator fun invoke(data: String) : ValidationResult {
        if(data.isEmpty()){
            return ValidationResult(
                successful = false,
                errorMessage = "Need to select an option"
            )
        }

        if(data.any { x -> x.isLowerCase() } || !data.any { x -> x.isDigit()  }){
            return ValidationResult(
                successful = false,
                errorMessage = "Option selected is invalid"
            )
        }

        return ValidationResult(true)
    }
}