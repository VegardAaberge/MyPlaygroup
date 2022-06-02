package com.myplaygroup.app.feature_admin.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_admin.presentation.chat.ChatScreen
import com.myplaygroup.app.feature_admin.presentation.classes.ClassesScreen
import com.myplaygroup.app.feature_admin.presentation.nav_drawer.NavDrawerBody
import com.myplaygroup.app.feature_admin.presentation.nav_drawer.NavDrawer
import com.myplaygroup.app.feature_admin.presentation.overview.OverviewScreen
import com.myplaygroup.app.feature_admin.presentation.plans.PlansScreen
import com.myplaygroup.app.feature_admin.presentation.users.UsersScreen
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
    val navController = rememberNavController()

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
            NavDrawerHeader(
                viewModel = adminViewModel
            )
            Divider(modifier = Modifier.fillMaxWidth())
            NavDrawerBody(
                items = NavDrawer.items.values.toList(),
                onItemClick = { route ->
                    when(route){
                        NavDrawer.LOGOUT -> adminViewModel.onEvent(AdminScreenEvent.logoutTapped)
                        else -> {
                            navController.navigate(
                                route = route,
                                builder = {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(NavDrawer.OVERVIEW)
                                }
                            )
                        }
                    }

                    // Close drawer
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        }
    ) {
        DrawerNavigation(navController)
    }
}

@Composable
fun DrawerNavigation(
    navController: NavHostController
) {
    NavHost(navController, startDestination = NavDrawer.OVERVIEW) {
        composable(NavDrawer.OVERVIEW) {
            OverviewScreen()
        }
        composable(NavDrawer.CLASSES) {
            ClassesScreen()
        }
        composable(NavDrawer.PLANS) {
            PlansScreen()
        }
        composable(NavDrawer.USERS) {
            UsersScreen()
        }
        composable(NavDrawer.CHAT) {
            ChatScreen()
        }
    }
}