package com.myplaygroup.app.feature_main.presentation.admin.create_plans

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.app_bar.AppBarBackButton
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun CreatePlansScreen(
    navigator: DestinationsNavigator,
    viewModel: CreatePlansViewModel = hiltViewModel()
) {
    val scaffoldState = collectEventFlow(viewModel, navigator)
    val state = viewModel.state

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar(
                title = "Generate",
                navigationIcon = {
                    AppBarBackButton(navigator)
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(CreatePlansScreenEvent.GenerateData)
                        },
                    ) {
                        Text(text = "Save")
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Create Plans")
        }
    }
}