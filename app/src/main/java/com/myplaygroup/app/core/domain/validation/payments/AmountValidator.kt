package com.myplaygroup.app.core.domain.validation.payments

import com.myplaygroup.app.core.domain.validation.ValidationResult
import kotlin.math.absoluteValue

class AmountValidator {
    operator fun invoke(amount: Int?) : ValidationResult {
        if(amount == null){
            return ValidationResult(
                successful = false,
                errorMessage = "Payments cannot be empty"
            )
        }

        if(amount.absoluteValue > 10000){
            return ValidationResult(
                successful = false,
                errorMessage = "Payments cannot be larger than 10,000"
            )
        }

        return ValidationResult(true)
    }
}