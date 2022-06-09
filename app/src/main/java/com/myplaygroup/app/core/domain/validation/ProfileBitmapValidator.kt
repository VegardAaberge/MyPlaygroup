package com.myplaygroup.app.core.domain.validation

import android.graphics.Bitmap

class ProfileBitmapValidator {
    operator fun invoke(bitmap: Bitmap?) : ValidationResult {
        if(bitmap == null){
            return ValidationResult(
                successful = false,
                errorMessage = "Profile picture need to be set"
            )
        }

        return ValidationResult(true)
    }
}