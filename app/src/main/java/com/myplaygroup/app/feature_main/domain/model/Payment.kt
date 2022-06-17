package com.myplaygroup.app.feature_main.domain.model

import java.time.LocalDate
import java.util.*

data class Payment(
    val clientId: String = UUID.randomUUID().toString(),
    val id: Long = -1,
    val username: String,
    val date: LocalDate,
    val amount: Long,
    val cancelled: Boolean = false,
    val modified: Boolean = id == -1L
)
