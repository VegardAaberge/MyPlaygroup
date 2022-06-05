package com.myplaygroup.app.feature_main.presentation.user.settings

sealed class SettingsScreenEvent {
    object EditProfilePictureTapped : SettingsScreenEvent()
    object LogoutButtonTapped : SettingsScreenEvent()
    object EditProfileTapped : SettingsScreenEvent()
}
