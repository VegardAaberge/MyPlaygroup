package com.myplaygroup.app.feature_main.presentation.admin.users

import com.myplaygroup.app.feature_main.domain.model.AppUser

data class UsersState (
    val appUsers: List<AppUser> = emptyList(),
    val showCreateUser: Boolean = false,

    val createErrorMessage: String? = null,
    val isLoading: Boolean = false
)