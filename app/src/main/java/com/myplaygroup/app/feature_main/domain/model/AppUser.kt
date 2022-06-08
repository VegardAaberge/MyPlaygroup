package com.myplaygroup.app.feature_main.domain.model

data class AppUser(
    val id: Long,
    val email: String,
    val locked: Boolean,
    val phoneNumber: String,
    val profileCreated: Boolean,
    val profileName: String,
    val userCredit: Long,
    val username: String
)