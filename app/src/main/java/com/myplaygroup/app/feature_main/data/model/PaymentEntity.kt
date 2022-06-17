package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
@Entity
data class PaymentEntity(
    @PrimaryKey val clientId: String = UUID.randomUUID().toString(),
    val id: Long = -1,
    val username: String,
    val date: String,
    val amount: Long,
    val cancelled: Boolean,

    @Transient
    val modified: Boolean = id == -1L
)
