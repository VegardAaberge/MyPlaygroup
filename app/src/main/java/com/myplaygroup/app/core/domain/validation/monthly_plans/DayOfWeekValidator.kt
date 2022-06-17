package com.myplaygroup.app.core.domain.validation.monthly_plans

import com.myplaygroup.app.core.domain.validation.ValidationResult
import java.time.DayOfWeek

class DayOfWeekValidator {
    operator fun invoke(data: Map<DayOfWeek, Boolean>) : ValidationResult {
        if(data.size != 6){
            return ValidationResult(
                successful = false,
                errorMessage = "Dayofweek is not set up correctly"
            )
        }

        if(data.all { x -> !x.value }){
            return ValidationResult(
                successful = false,
                errorMessage = "Need to select a weekday"
            )
        }

        if(data.all { x -> x.value }){
            return ValidationResult(
                successful = false,
                errorMessage = "Cannot select all weekdays"
            )
        }

        return ValidationResult(true)
    }
}