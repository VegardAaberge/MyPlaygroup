package com.myplaygroup.app.feature_admin.presentation

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import com.myplaygroup.app.feature_admin.presentation.nav_drawer.NavDrawer


@Composable
fun AppBar(
    route: String,
    onNavigationIconClick: () -> Unit,
){
    val currentPageTitle = NavDrawer.items[route]?.title ?: "Admin Panel"

    TopAppBar(
        title = {
            Text(text = currentPageTitle)
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer"
                )
            }
        }
    )
}