package com.myplaygroup.app.feature_main.presentation.util

sealed class Screen(val route: String){
    object HomeFragment : Screen("home")
    object ChatFragment : Screen("chat")
    object SettingsFragment : Screen("settings")
}
