package com.myplaygroup.app.core.domain.validation.user

import android.content.Context
import android.graphics.Bitmap
import com.myplaygroup.app.R
import com.myplaygroup.app.core.domain.validation.ValidationResult

class ProfileBitmapValidator(val context: Context) {
    operator fun invoke(bitmap: Bitmap?) : ValidationResult {
        if(bitmap == null){
            return ValidationResult(
                successful = false,
                errorMessage = context.getString(R.string.validation_profile_picture_not_set)
            )
        }

        return ValidationResult(true)
    }
}