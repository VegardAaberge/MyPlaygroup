package com.myplaygroup.app.feature_admin.presentation

sealed class AdminScreenEvent {
    object EditProfilePictureTapped : AdminScreenEvent()
    object EditProfileTapped : AdminScreenEvent()
    object logoutTapped : AdminScreenEvent()
    data class routeUpdated(val route: String) : AdminScreenEvent()
}