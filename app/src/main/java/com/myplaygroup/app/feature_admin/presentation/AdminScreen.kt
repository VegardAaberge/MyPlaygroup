package com.myplaygroup.app.feature_admin.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun AdminScreen(
    navigator: DestinationsNavigator,
    adminViewModel: AdminViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = collectEventFlow(adminViewModel, navigator)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                onNavigationIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            DrawerHeader(
                viewModel = adminViewModel
            )
            Divider(modifier = Modifier.fillMaxWidth())
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id = "overview",
                        title = "Overview",
                        contentDescription = "Go to overview screen",
                        icon = Icons.Default.Home
                    ),
                    MenuItem(
                        id = "classes",
                        title = "Classes",
                        contentDescription = "Go to classes screen",
                        icon = Icons.Default.DateRange
                    ),
                    MenuItem(
                        id = "plans",
                        title = "Plans",
                        contentDescription = "Go to plans screen",
                        icon = Icons.Default.List
                    ),
                    MenuItem(
                        id = "users",
                        title = "Users",
                        contentDescription = "Go to users screen",
                        icon = Icons.Default.Person
                    ),
                    MenuItem(
                        id = "chat",
                        title = "Chat",
                        contentDescription = "Go to chat screen",
                        icon = Icons.Default.Email
                    ),
                    MenuItem(
                        id = "logout",
                        title = "Logout",
                        contentDescription = "Logout app",
                        icon = Icons.Default.ExitToApp
                    )
                ),
                onItemClick = {
                    when(it.id){
                        "home" -> println("Clicked on ${it.title}")
                        "settings" -> println("Clicked on ${it.title}")
                        "help" -> println("Clicked on ${it.title}")
                    }
                }
            )
        }
    ) {

    }
}