package com.myplaygroup.app.core.domain.validation.payments

import com.myplaygroup.app.core.domain.validation.ValidationResult

class AmountValidator {
    operator fun invoke(amount: Int?) : ValidationResult {
        if(amount == null){
            return ValidationResult(
                successful = false,
                errorMessage = "Payments cannot be empty"
            )
        }

        if(amount <= 0){
            return ValidationResult(
                successful = false,
                errorMessage = "Payments must be larger than 0"
            )
        }

        if(amount > 10000){
            return ValidationResult(
                successful = false,
                errorMessage = "Payments cannot be larger than 10,000"
            )
        }

        return ValidationResult(true)
    }
}