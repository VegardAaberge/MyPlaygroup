package com.myplaygroup.app.feature_main.presentation.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.chat.ChatScreen
import com.myplaygroup.app.feature_main.presentation.user.home.HomeScreen
import com.myplaygroup.app.feature_main.presentation.user.settings.SettingsScreen
import com.myplaygroup.app.feature_main.presentation.user.util.Screen
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

    val homeValue = stringResource(id = R.string.bar_home)
    var title by remember{
        mutableStateOf(homeValue)
    }


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar(title)
        },
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(
                        name = homeValue,
                        route = Screen.HomeFragment.route,
                        outlinedIcon = painterResource(id = R.drawable.ic_outline_home_24),
                        filledIcon = painterResource(id = R.drawable.ic_baseline_home_24),
                    ),
                    BottomNavItem(
                        name = stringResource(R.string.bar_chat),
                        route = Screen.ChatFragment.route,
                        outlinedIcon = painterResource(id = R.drawable.ic_baseline_chat_bubble_outline_24),
                        filledIcon = painterResource(id = R.drawable.ic_baseline_chat_bubble_24)
                    ),
                    BottomNavItem(
                        name = stringResource(R.string.bar_settings),
                        route = Screen.SettingsFragment.route,
                        outlinedIcon = painterResource(id = R.drawable.ic_outline_person_24),
                        filledIcon = painterResource(id = R.drawable.ic_baseline_person_24),
                    ),
                ),
                navController = navController,
                onItemClick = { item ->
                    title = item.name
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
        BottomNavigationScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
private fun BottomNavigationScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeFragment.route
    ){
        composable(Screen.HomeFragment.route){
            HomeScreen(viewModel)
        }
        composable(Screen.ChatFragment.route){
            ChatScreen(viewModel)
        }
        composable(Screen.SettingsFragment.route){
            SettingsScreen(viewModel)
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
                                    painter = icon,
                                    contentDescription = item.name,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }else{
                            Icon(
                                painter = icon,
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




