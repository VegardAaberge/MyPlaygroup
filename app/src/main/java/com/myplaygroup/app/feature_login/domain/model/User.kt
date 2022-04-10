package com.myplaygroup.app.feature_login.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey val id: Int? = null,
    val token: String,
)