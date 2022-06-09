package com.myplaygroup.app.feature_main.presentation.admin

import com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.ParametersType

sealed class AdminScreenEvent {
    object EditProfilePictureTapped : AdminScreenEvent()
    object EditProfileTapped : AdminScreenEvent()
    object logoutTapped : AdminScreenEvent()
    data class routeUpdated(val route: String) : AdminScreenEvent()
    data class NavigateToEditScreen(
        val type: ParametersType,
        val id: Long
    ) : AdminScreenEvent()
}