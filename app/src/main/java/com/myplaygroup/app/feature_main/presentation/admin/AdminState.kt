package com.myplaygroup.app.feature_main.presentation.admin

import android.net.Uri
import androidx.compose.ui.graphics.vector.ImageVector

data class AdminState (
    val adminUri: Uri? = null,
    val updateIcons: Boolean = true,
    val title: String = "",
    val actionButtons: List<ActionButton> = listOf(),
){
    data class ActionButton(
        val action: () -> Unit = {},
        val icon: ImageVector
    )
}

