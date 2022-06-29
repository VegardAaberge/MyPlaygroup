package com.myplaygroup.app.feature_profile.data.requests

import com.myplaygroup.app.feature_profile.domain.model.EditProfileType

data class ProfileRequest (
    val profileName : String? = null,
    val password : String? = null,
    val phoneNumber : String? = null,
    val editProfileType: EditProfileType
)