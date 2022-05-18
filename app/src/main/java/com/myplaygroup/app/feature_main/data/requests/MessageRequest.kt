package com.myplaygroup.app.feature_main.data.requests

data class MessageRequest(
    val message: String,
    val receivers: List<String>
)
