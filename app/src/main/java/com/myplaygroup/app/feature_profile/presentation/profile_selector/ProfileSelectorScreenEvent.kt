package com.myplaygroup.app.feature_profile.presentation.profile_selector

import android.graphics.Bitmap

sealed class ProfileSelectorScreenEvent {
    data class TakePictureDone(val bitmap: Bitmap) : ProfileSelectorScreenEvent()
}