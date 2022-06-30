package com.myplaygroup.app.core.domain.validation.user

import android.content.Context
import com.myplaygroup.app.R
import com.myplaygroup.app.core.domain.validation.ValidationResult

class PasswordValidator(val context: Context) {
    operator fun invoke(password: String) : ValidationResult {
        if(password.length < 8){
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validation_password_at_least_8_characters)
            )
        }
        val constainsLetterAndDigits = password.any { it.isDigit() }
                && password.any{ it.isLetter() }
                && password.any { it.isUpperCase() }
                && password.any { it.isLowerCase() }


        if(!constainsLetterAndDigits){
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validation_password_correct_format)
            )
        }
        return ValidationResult(true)
    }
}