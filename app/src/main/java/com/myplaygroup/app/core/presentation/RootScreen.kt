package com.myplaygroup.app.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.data.pref.UserSettings
import com.myplaygroup.app.destinations.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first


@Destination(start = true)
@Composable
fun RootScreen(
    navigator: DestinationsNavigator,
    viewModel: RootViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = true){
        if(viewModel.isAuthenticated()){
            popAndNavigateTo(navigator, MainScreenDestination)
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