package com.myplaygroup.app.feature_main.domain.use_cases

import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.EditItem
import com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.ParametersType

data class EditUseCases(
    val fetchParameterItems: (type: ParametersType, id: Long) -> Resource<List<EditItem>> = { a, b -> Resource.Success(listOf(
        EditItem(
            key = "key1",
            type = "string",
            value = "value1",
            error = null
        ),
        EditItem(
            key = "key2",
            type = "string",
            value = "value2",
            error = null
        )
    )) },
    val validateParameters: (editParameters: List<EditItem>) -> List<EditItem> = { it },
    val storeParameterItems: () -> Resource<String> = { Resource.Success("") },
)
