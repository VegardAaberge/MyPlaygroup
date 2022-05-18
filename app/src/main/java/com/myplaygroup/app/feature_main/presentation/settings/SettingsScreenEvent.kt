package com.myplaygroup.app.feature_main.presentation.settings

import com.myplaygroup.app.feature_main.presentation.home.HomeScreenEvent

sealed class SettingsScreenEvent {
    object EditProfilePictureTapped : SettingsScreenEvent()
    object LogoutButtonTapped : SettingsScreenEvent()
    object EditProfileTapped : SettingsScreenEvent()
}
