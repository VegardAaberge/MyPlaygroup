package com.myplaygroup.app.feature_login.data.remote.responses

data class LoginResponse (
    val successful: Boolean,
    val message: String,
    val createProfile: Boolean
)