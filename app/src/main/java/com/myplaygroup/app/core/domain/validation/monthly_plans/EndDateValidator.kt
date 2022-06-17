package com.myplaygroup.app.core.domain.validation.monthly_plans

import com.myplaygroup.app.core.domain.validation.ValidationResult
import java.time.LocalDate

class EndDateValidator {
    operator fun invoke(endDate: LocalDate, startDate: LocalDate) : ValidationResult {
        if(endDate < LocalDate.of(2022, 1, 1) || endDate > LocalDate.of(2030, 1, 1)){
            return ValidationResult(
                successful = false,
                errorMessage = "Need to be between 2022 to 2030"
            )
        }

        if(endDate <= startDate){
            return ValidationResult(
                successful = false,
                errorMessage = "End date is before start date"
            )
        }

        return ValidationResult(true)
    }
}