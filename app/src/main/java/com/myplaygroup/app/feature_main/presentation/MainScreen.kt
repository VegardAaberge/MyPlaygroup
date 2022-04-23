package com.myplaygroup.app.feature_main.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.myplaygroup.app.core.presentation.TopAppBar.AppBarMenuButton
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.chat.ChatScreen
import com.myplaygroup.app.feature_main.presentation.home.HomeScreen
import com.myplaygroup.app.feature_main.presentation.settings.SettingsScreen
import com.myplaygroup.app.feature_main.presentation.util.Screen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val scaffoldState = collectEventFlow(viewModel, navigator)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar("Main Screen", navigationIcon = {
                AppBarMenuButton()
            })
        },
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(
                        name = "Home",
                        route = Screen.HomeFragment.route,
                        outlinedIcon = Icons.Outlined.Home,
                        filledIcon = Icons.Default.Home,
                    ),
                    BottomNavItem(
                        name = "Chat",
                        route = Screen.ChatFragment.route,
                        outlinedIcon = Icons.Outlined.Notifications,
                        filledIcon = Icons.Default.Notifications,
                    ),
                    BottomNavItem(
                        name = "Settings",
                        route = Screen.SettingsFragment.route,
                        outlinedIcon = Icons.Outlined.Settings,
                        filledIcon = Icons.Default.Settings,
                    ),
                ),
                navController = navController,
                onItemClick = { item ->
                    navController.navigate(item.route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    ) {
        BottomNavigationScreen(navController = navController)
    }
}

@Composable
private fun BottomNavigationScreen(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ){
        composable(Screen.HomeFragment.route){
            HomeScreen()
        }
        composable(Screen.ChatFragment.route){
            ChatScreen()
        }
        composable(Screen.SettingsFragment.route){
            SettingsScreen()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier
            .height(65.dp),
        backgroundColor = Color(0xFFF0F0F0),
        elevation = 5.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            val icon = if(selected) item.filledIcon else item.outlinedIcon

            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = Color(0xFF00AA00),
                unselectedContentColor = Color(0xFF666666),
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        if(item.badgeCount > 0){
                            BadgedBox(
                                badge = {
                                    Badge(
                                        backgroundColor = Color(0xFFDD0000),
                                    ){
                                        Text(
                                            color = Color.White,
                                            text = item.badgeCount.toString()
                                        )
                                    }
                                })
                            {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = item.name,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }else{
                            Icon(
                                imageVector = icon,
                                contentDescription = item.name,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        if(selected){
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            )
        }
    }
}




