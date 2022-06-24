package com.myplaygroup.app.feature_main.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val id: Long,
    val clientId: String,
    val createdBy: String,
    val message: String,
    val receivers: List<String>,
    val readBy: List<String>
)
