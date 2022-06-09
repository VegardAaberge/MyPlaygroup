package com.myplaygroup.app.feature_main.domain.model

import com.myplaygroup.app.feature_main.domain.interactors.enums.ParameterDisplayType

data class ParameterItem(
    val type: ParameterDisplayType,
    val key: String,
    val value: Any,
    val error: String? = null
)
