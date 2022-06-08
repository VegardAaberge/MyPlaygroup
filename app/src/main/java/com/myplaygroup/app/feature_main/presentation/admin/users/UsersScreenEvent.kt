package com.myplaygroup.app.feature_main.presentation.admin.users

sealed class UsersScreenEvent {
    data class CreateUserDialog(val show: Boolean) : UsersScreenEvent()
}
