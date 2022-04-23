package com.myplaygroup.app.feature_main.presentation.home

sealed class HomeScreenEvent {
    object LogoutButtonTapped : HomeScreenEvent()
}
