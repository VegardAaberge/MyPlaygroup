package com.myplaygroup.app.feature_profile.data.model

import android.provider.ContactsContract.CommonDataKinds.Email
import com.myplaygroup.app.core.domain.settings.UserRole


data class AppUser (
    val id: Long = -1,
    val username: String,
    val profileName: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val password: String? = null,
    val appUserRole: UserRole? = null,
    val userCredit: Long = 0L,
    val locked: Boolean = false,
    val enabled: Boolean = true,
    val profileCreated: Boolean = false
)