package com.myplaygroup.app.feature_main.presentation.admin

import android.net.Uri
import androidx.compose.ui.graphics.vector.ImageVector

data class AdminState (
    val adminUri: Uri? = null,
    val title: String = "",
    val actionButton: ActionButton? = null
){
    data class ActionButton(
        val action: () -> Unit = {},
        val icon: ImageVector
    )
}

