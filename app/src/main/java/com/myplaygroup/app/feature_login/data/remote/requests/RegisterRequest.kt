package com.myplaygroup.app.feature_login.data.remote.requests

data class RegisterRequest (
    val username: String,
    val password: String,
    val email: String
)