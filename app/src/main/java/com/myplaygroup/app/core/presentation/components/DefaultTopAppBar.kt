package com.myplaygroup.app.core.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation
import com.myplaygroup.app.NavGraphs
import com.myplaygroup.app.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun DefaultTopAppBar(
    title: String,
    navigator: DestinationsNavigator,
    hasBackButton : Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
) {
    if(hasBackButton){
        TopAppBar(
            title = {
                Text(text = title)
            },
            navigationIcon = if(hasBackButton){
                @Composable{
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
            }else null,
            actions = actions,
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            elevation = 12.dp
        )
    }else{
        TopAppBar(
            title = {
                Text(text = title)
            },
            actions = actions,
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            elevation = 12.dp
        )
    }

}