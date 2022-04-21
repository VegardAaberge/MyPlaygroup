package com.myplaygroup.app.feature_main.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.myplaygroup.app.core.presentation.components.CollectEventFlow
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    viewModel: MainViewModel = hiltViewModel()
) {
    val imageUri = viewModel.state.imageUri

    val scaffoldState = CollectEventFlow(viewModel, navigator)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar("Main Screen", navigator, false)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(data = imageUri),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
            )

            Button(onClick = {
                viewModel.onEvent(MainScreenEvent.LogoutButtonTapped)
            }) {
                Text(text = "Logout")
            }
        }
    }
}