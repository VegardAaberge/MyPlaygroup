package com.myplaygroup.app.feature_admin.presentation

sealed class AdminScreenEvent {
    object EditProfilePictureTapped : AdminScreenEvent()
    object EditProfileTapped : AdminScreenEvent()
    object logoutTapped : AdminScreenEvent()
}