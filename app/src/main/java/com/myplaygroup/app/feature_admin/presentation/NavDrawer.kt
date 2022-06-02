package com.myplaygroup.app.feature_admin.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object NavDrawer{

    const val OVERVIEW = "overview"
    const val CLASSES = "classes"
    const val PLANS = "plans"
    const val USERS = "users"
    const val CHAT = "chat"
    const val LOGOUT = "logout"

    val items = linkedMapOf<String, NavDrawerItem>(
        OVERVIEW to NavDrawerItem(OVERVIEW, Icons.Default.Home, "Overview"),
        CLASSES to NavDrawerItem(CLASSES, Icons.Default.List, "Classes"),
        PLANS to NavDrawerItem(PLANS, Icons.Default.DateRange, "Plans"),
        USERS to NavDrawerItem(USERS, Icons.Default.Person, "Users"),
        CHAT to NavDrawerItem(CHAT, Icons.Default.Notifications, "Chat"),
        LOGOUT to NavDrawerItem(LOGOUT, Icons.Default.ExitToApp, "Logout"),
    )
}