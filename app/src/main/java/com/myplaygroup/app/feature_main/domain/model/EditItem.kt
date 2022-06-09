package com.myplaygroup.app.feature_main.domain.model

data class EditItem(
    val key: String,
    val type: String,
    val value: String,
    val error: String?
)
