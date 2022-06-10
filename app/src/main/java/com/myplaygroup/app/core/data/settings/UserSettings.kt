package com.myplaygroup.app.core.data.settings

import com.myplaygroup.app.core.util.Constants.NO_VALUE
import kotlinx.serialization.Serializable

@Serializable
data class UserSettings (
    val refreshToken: String = NO_VALUE,
    val accessToken: String  = NO_VALUE,
    val username: String  = NO_VALUE,
    val userRole: String  = NO_VALUE,
    val profileName: String  = NO_VALUE,
    val phoneNumber: String  = NO_VALUE,
)