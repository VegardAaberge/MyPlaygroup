package com.myplaygroup.app.core.presentation.app_bar

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable

@Composable
fun AppBarMenuButton() {
    IconButton(
        onClick = {

        }
    ) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Back Button",
        )
    }
}