package com.myplaygroup.app.feature_main.presentation.user

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name: String,
    val route: String,
    val outlinedIcon: Painter,
    val badgeCount: Int = 0,
    val filledIcon: Painter
)