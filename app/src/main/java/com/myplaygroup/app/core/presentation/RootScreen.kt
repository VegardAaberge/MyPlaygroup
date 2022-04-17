package com.myplaygroup.app.core.presentation

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.util.Constants.KEY_PASSWORD
import com.myplaygroup.app.core.util.Constants.KEY_USERNAME
import com.myplaygroup.app.core.util.Constants.NO_PASSWORD
import com.myplaygroup.app.core.util.Constants.NO_USERNAME
import com.myplaygroup.app.destinations.*
import com.myplaygroup.app.feature_login.presentation.login.LoginScreen
import com.myplaygroup.app.feature_main.presentation.MainScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import javax.inject.Inject


@Destination(start = true)
@Composable
fun RootScreen(
    navigator: DestinationsNavigator,
    viewModel: RootViewModel = hiltViewModel(),
) {
    if(viewModel.IsAuthenticated()){
        PopAndNavigateTo(navigator, MainScreenDestination)
    }else{
        PopAndNavigateTo(navigator, LoginScreenDestination)
    }
}

fun PopAndNavigateTo(navigator: DestinationsNavigator, destination: DirectionDestination){
    navigator.navigate(destination){
        popUpTo(RootScreenDestination.route){
            inclusive = true
        }
    }
}