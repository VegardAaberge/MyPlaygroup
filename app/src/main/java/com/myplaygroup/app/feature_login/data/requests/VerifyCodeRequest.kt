package com.myplaygroup.app.feature_login.data.requests

data class VerifyCodeRequest (
    val token: String,
    val code: String
)