package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters

sealed class EditParametersScreenEvent {
    data class UpdateValue(val value: Any, val key: String) : EditParametersScreenEvent()
    object SaveData : EditParametersScreenEvent()
}