package com.myplaygroup.app.feature_main.presentation.admin.nav_drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

object NavDrawer{

    const val CHAT = "chat"
    const val CLASSES = "classes"
    const val PLANS = "plans"
    const val PAYMENTS = "payments"
    const val USERS = "users"
    const val LOGOUT = "logout"

    val items = linkedMapOf<String, NavDrawerItem>(
        CHAT to NavDrawerItem(CHAT, Icons.Default.Share, "Chat"),
        CLASSES to NavDrawerItem(CLASSES, Icons.Default.DateRange, "Classes"),
        PLANS to NavDrawerItem(PLANS, Icons.Default.List, "Plans"),
        PAYMENTS to NavDrawerItem(PAYMENTS, Icons.Default.MailOutline, "Payments"),
        USERS to NavDrawerItem(USERS, Icons.Default.Person, "Users"),
        LOGOUT to NavDrawerItem(LOGOUT, Icons.Default.ExitToApp, "Logout"),
    )
}