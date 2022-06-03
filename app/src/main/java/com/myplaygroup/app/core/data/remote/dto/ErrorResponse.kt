package com.myplaygroup.app.core.data.remote.dto

import java.time.LocalDateTime


@kotlinx.serialization.Serializable
data class ErrorResponse (
    val timestamp: String = "",
    val code: Int,
    val status: String = "",
    val message: String,
    val data: Map<String, String>? = null
)