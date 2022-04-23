package com.myplaygroup.app.core.presentation.TopAppBar

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

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