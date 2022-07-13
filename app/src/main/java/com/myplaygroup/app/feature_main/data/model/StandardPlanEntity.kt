package com.myplaygroup.app.feature_main.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myplaygroup.app.feature_main.domain.enums.DailyClassType
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class StandardPlanEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val price: Int,
    val type: DailyClassType
)
