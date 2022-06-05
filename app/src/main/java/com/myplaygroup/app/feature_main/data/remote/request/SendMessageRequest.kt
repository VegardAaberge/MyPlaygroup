package com.myplaygroup.app.feature_main.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val clientId: String,
    val message: String,
    val receivers: List<String>
)
