package com.myplaygroup.app.feature_main.presentation.user.settings

import com.myplaygroup.app.feature_profile.domain.model.EditProfileType

sealed class SettingsScreenEvent {
    object EditProfilePictureTapped : SettingsScreenEvent()
    object LogoutButtonTapped : SettingsScreenEvent()
    data class EditProfileTapped(val editProfileType: EditProfileType) : SettingsScreenEvent()
}
