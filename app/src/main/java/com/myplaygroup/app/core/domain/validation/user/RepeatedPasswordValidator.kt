package com.myplaygroup.app.core.domain.validation.user

import android.content.Context
import com.myplaygroup.app.R
import com.myplaygroup.app.core.domain.validation.ValidationResult

class RepeatedPasswordValidator(val context: Context) {
    operator fun invoke(password: String, repeatedPassword: String) : ValidationResult {
        if(password != repeatedPassword){
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validation_password_does_not_match)
            )
        }
        return ValidationResult(true)
    }
}