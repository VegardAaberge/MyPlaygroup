package com.myplaygroup.app.feature_main.domain.model

data class Message (
    val message: String,
    val owner: String,
    val receivers: List<String>,
    val created: Long,
)