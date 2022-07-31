package com.myplaygroup.app.feature_main.presentation.admin.users

sealed class UsersScreenEvent {
    object RefreshData : UsersScreenEvent()
    object UploadAppUsers : UsersScreenEvent()
    data class CreateUserDialog(val show: Boolean) : UsersScreenEvent()
    data class CreateUser(val username: String) : UsersScreenEvent()
    data class OnSearchChanged(val searchValue: String) : UsersScreenEvent()
    object TriggerSearch : UsersScreenEvent()
}
