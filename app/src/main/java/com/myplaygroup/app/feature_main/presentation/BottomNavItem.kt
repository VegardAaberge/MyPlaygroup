package com.myplaygroup.app.feature_main.presentation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name: String,
    val route: String,
    val outlinedIcon: ImageVector,
    val badgeCount: Int = 0,
    val filledIcon: ImageVector
)