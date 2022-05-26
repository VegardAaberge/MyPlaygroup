package com.myplaygroup.app.feature_main.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse (
    val id: Long,
    val clientId: String,
    val message: String,
    val profileName: String?,
    val created: String,
    val createdBy: String
)