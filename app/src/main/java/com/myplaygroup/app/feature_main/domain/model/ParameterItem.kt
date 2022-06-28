package com.myplaygroup.app.feature_main.domain.model

import com.myplaygroup.app.core.util.TextUtils
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType

data class ParameterItem(
    val type: ParameterDisplayType,
    val key: String,
    val value: Any,
    val enabled: Boolean = true,
    var error: String? = null,
){
    fun getTitle() : String {
        return TextUtils.deCamelCasealize(key).replaceFirstChar { x -> x.uppercase() }
    }
}
