package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
@Entity
data class AppUserEntity(
    @PrimaryKey val clientId: String = UUID.randomUUID().toString(),
    val id: Long = -1,
    val username: String,
    val locked: Boolean = false,
    val userCredit: Long = 0,
    val profileCreated: Boolean = false,
    val profileName: String? = null,
    val phoneNumber: String? = null,
    val resetPassword: Boolean = false,

    @Transient
    val modified: Boolean = id == -1L
)