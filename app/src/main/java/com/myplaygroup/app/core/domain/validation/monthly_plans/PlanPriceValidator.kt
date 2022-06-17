package com.myplaygroup.app.core.domain.validation.monthly_plans

import com.myplaygroup.app.core.domain.validation.ValidationResult

class PlanPriceValidator {
    operator fun invoke(price: Int) : ValidationResult {
        if(price < 0){
            return ValidationResult(
                successful = false,
                errorMessage = "Price cannot be negative"
            )
        }

        if(price > 2000){
            return ValidationResult(
                successful = false,
                errorMessage = "Price cannot be larger than 2000"
            )
        }

        return ValidationResult(true)
    }
}