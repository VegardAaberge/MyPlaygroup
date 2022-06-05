package com.myplaygroup.app.feature_main.data.remote.response


@kotlinx.serialization.Serializable
data class ErrorResponse (
    val timestamp: String = "",
    val code: Int,
    val status: String = "",
    val message: String,
    val data: Map<String, String>? = null
)