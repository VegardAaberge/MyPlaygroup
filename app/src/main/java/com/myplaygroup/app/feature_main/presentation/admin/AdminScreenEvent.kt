package com.myplaygroup.app.feature_main.presentation.admin

import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.domain.model.ChatGroup

sealed class AdminScreenEvent {
    object EditProfilePictureTapped : AdminScreenEvent()
    object EditProfileTapped : AdminScreenEvent()
    object LogoutTapped : AdminScreenEvent()
    object NavigateToCreateMonthlyPlan : AdminScreenEvent()
    data class routeUpdated(val route: String) : AdminScreenEvent()
    data class NavigateToEditScreen(
        val type: ParametersType,
        val clientId: String
    ) : AdminScreenEvent()
    data class NavigateToChatScreen(val chatGroup: ChatGroup) : AdminScreenEvent()
}