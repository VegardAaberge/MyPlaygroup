package com.myplaygroup.app.core.domain.validation.daily_class

import com.myplaygroup.app.core.domain.validation.ValidationResult
import java.time.LocalTime

class EndTimeValidator {
    operator fun invoke(endTime: LocalTime, startTime: LocalTime) : ValidationResult {
        if(endTime < LocalTime.of(9, 0, 0) || endTime > LocalTime.of(21, 0, 0)){
            return ValidationResult(
                successful = false,
                errorMessage = "Need to be between 7 to 21"
            )
        }

        if(endTime <= startTime){
            return ValidationResult(
                successful = false,
                errorMessage = "End time is before start time"
            )
        }

        return ValidationResult(true)
    }
}