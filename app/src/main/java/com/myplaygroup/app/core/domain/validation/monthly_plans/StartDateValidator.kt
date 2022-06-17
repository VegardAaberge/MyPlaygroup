package com.myplaygroup.app.core.domain.validation.monthly_plans

import com.myplaygroup.app.core.domain.validation.ValidationResult
import java.time.LocalDate

class StartDateValidator {
    operator fun invoke(date: LocalDate) : ValidationResult {
        if(date < LocalDate.of(2022, 1, 1) || date > LocalDate.of(2030, 1, 1)){
            return ValidationResult(
                successful = false,
                errorMessage = "Need to be between 2022 to 2030"
            )
        }

        return ValidationResult(true)
    }
}