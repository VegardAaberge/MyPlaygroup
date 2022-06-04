package com.myplaygroup.app.feature_profile.domain.use_cases.validators

import android.graphics.Bitmap
import com.myplaygroup.app.feature_profile.domain.use_cases.ValidationResult

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