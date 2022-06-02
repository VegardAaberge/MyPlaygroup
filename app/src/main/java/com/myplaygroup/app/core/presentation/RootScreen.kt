package com.myplaygroup.app.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.domain.settings.UserRole
import com.myplaygroup.app.destinations.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination(start = true)
@Composable
fun RootScreen(
    navigator: DestinationsNavigator,
    viewModel: RootViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = true){
        val userSettings = viewModel.getUserSettingsWithRole()
        if(userSettings != null){
            if(userSettings.userRole == UserRole.ADMIN.name){
                popAndNavigateTo(navigator, AdminScreenDestination)
            }else{
                popAndNavigateTo(navigator, MainScreenDestination)
            }

        }else{
            popAndNavigateTo(navigator, LoginScreenDestination)
        }
    }
}

private fun popAndNavigateTo(navigator: DestinationsNavigator, destination: DirectionDestination){
    navigator.navigate(destination){
        popUpTo(RootScreenDestination.route){
            inclusive = true
        }
    }
}