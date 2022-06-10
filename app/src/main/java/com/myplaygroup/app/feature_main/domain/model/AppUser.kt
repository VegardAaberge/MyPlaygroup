package com.myplaygroup.app.feature_main.domain.model

import java.util.*

data class AppUser(
    val clientId: String = UUID.randomUUID().toString(),
    val id: Long = -1,
    val locked: Boolean,
    val phoneNumber: String,
    val profileCreated: Boolean,
    val profileName: String,
    val userCredit: Long,
    val username: String,
    val modified: Boolean = id == -1L
)