package com.myplaygroup.app.core.domain.validation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
