package com.myplaygroup.app.feature_login.data.remote.responses

data class LoginResponse (
    val access_token: String,
    val refresh_token: String,
    val profile_created: Boolean,
    val profile_name: String,
    val email: String,
    val phone_number: String
)