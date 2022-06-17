package com.myplaygroup.app.core.domain.validation.daily_class

import com.myplaygroup.app.core.domain.validation.ValidationResult
import java.time.LocalTime

class StartTimeValidator {
    operator fun invoke(startTime: LocalTime) : ValidationResult {
        if(startTime < LocalTime.of(9, 0, 0) || startTime > LocalTime.of(21, 0, 0)){
            return ValidationResult(
                successful = false,
                errorMessage = "Need to be between 7 to 21"
            )
        }

        return ValidationResult(true)
    }
}