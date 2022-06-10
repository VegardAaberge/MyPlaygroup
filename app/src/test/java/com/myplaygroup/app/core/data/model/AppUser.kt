package com.myplaygroup.app.core.data.model

import com.myplaygroup.app.core.domain.settings.UserRole


data class AppUser (
    val id: Long = -1,
    val username: String,
    val profileName: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null,
    val appUserRole: UserRole = UserRole.USER,
    val userCredit: Long = 0L,
    val locked: Boolean = false,
    val enabled: Boolean = true,
    val profileCreated: Boolean = false
)