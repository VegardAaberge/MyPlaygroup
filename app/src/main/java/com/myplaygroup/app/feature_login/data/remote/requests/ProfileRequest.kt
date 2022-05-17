package com.myplaygroup.app.feature_login.data.remote.requests

data class ProfileRequest (
    val profileName : String,
    val password : String,
    val phoneNumber : String,
    val email : String
)