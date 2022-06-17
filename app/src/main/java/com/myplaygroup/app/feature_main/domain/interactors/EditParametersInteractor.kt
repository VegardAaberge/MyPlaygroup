package com.myplaygroup.app.feature_main.domain.interactors

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.domain.model.ParameterItem

interface EditParametersInteractor {

    suspend fun fetchParameterItems(
        type: ParametersType,
        id: String
    ) : Resource<List<ParameterItem>>

    fun validateParameters(
        parameterItems: List<ParameterItem>,
        type: ParametersType,
    ) : MutableList<ParameterItem>

    suspend fun storeParameterItems(
        parameterItems: List<ParameterItem>,
        type: ParametersType,
    ): Resource<Unit>

    suspend fun deleteItem(
        id: String,
        type: ParametersType
    ): Resource<Unit>
}