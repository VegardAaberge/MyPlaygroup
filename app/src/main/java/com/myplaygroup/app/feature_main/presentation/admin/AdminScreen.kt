package com.myplaygroup.app.feature_main.presentation.admin

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_admin.presentation.NavDrawerHeader
import com.myplaygroup.app.feature_main.presentation.admin.chat.ChatScreen
import com.myplaygroup.app.feature_main.presentation.admin.classes.ClassesScreen
import com.myplaygroup.app.feature_main.presentation.admin.monthly_plans.MonthlyPlanScreen
import com.myplaygroup.app.feature_main.presentation.admin.nav_drawer.NavDrawer
import com.myplaygroup.app.feature_main.presentation.admin.nav_drawer.NavDrawerBody
import com.myplaygroup.app.feature_main.presentation.admin.overview.OverviewScreen
import com.myplaygroup.app.feature_main.presentation.admin.users.UsersScreen
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

    val state = adminViewModel.state
    val title = adminViewModel.state.title
    val actionButtons = adminViewModel.state.actionButtons

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = title)
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Toggle drawer"
                        )
                    }
                },
                actions = {
                    actionButtons.forEach { actionButton ->
                        IconButton(
                            onClick = actionButton.action,
                        ) {
                            Icon(
                                imageVector = actionButton.icon,
                                contentDescription = null
                            )
                        }
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
                            adminViewModel.onEvent(
                                AdminScreenEvent.routeUpdated(route)
                            )
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
        DrawerNavigation(navController, adminViewModel)
    }
}

@Composable
fun DrawerNavigation(
    navController: NavHostController,
    adminViewModel: AdminViewModel
) {

    NavHost(navController, startDestination = NavDrawer.USERS) {
        composable(NavDrawer.OVERVIEW) {
            OverviewScreen(adminViewModel)
        }
        composable(NavDrawer.CLASSES) {
            ClassesScreen(adminViewModel)
        }
        composable(NavDrawer.PLANS) {
            MonthlyPlanScreen(adminViewModel)
        }
        composable(NavDrawer.USERS) {
            UsersScreen(adminViewModel)
        }
        composable(NavDrawer.CHAT) {
            ChatScreen(adminViewModel)
        }
    }
}