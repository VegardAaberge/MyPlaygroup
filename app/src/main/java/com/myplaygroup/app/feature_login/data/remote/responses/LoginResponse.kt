package com.myplaygroup.app.feature_login.data.remote.responses

data class LoginResponse (
    val successful: Boolean,
    val message: String,
    var email: String = "",
    var profileName: String = "",
    var phoneNumber: String = "",
    val createProfile: Boolean
)