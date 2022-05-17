package com.myplaygroup.app.feature_main.data.remote

data class MessageRequest(
    val message: String,
    val receivers: List<String>
)
