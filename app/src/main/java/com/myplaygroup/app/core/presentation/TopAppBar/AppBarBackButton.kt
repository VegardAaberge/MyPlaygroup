package com.myplaygroup.app.core.presentation.TopAppBar

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun AppBarBackButton(
    navigator: DestinationsNavigator
) {
    IconButton(
        onClick = {
            navigator.popBackStack()
        }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back Button",
        )
    }
}