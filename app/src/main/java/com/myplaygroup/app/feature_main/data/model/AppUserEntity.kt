package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class AppUserEntity(
    @PrimaryKey val id: Long,
    val username: String,
    val locked: Boolean,
    val userCredit: Long,
    val profileCreated: Boolean,
    val profileName: String?,
    val email: String?,
    val phoneNumber: String?,

)