package com.myplaygroup.app.feature_main.presentation.settings

sealed class SettingsScreenEvent {
    object LogoutButtonTapped : SettingsScreenEvent()
}
