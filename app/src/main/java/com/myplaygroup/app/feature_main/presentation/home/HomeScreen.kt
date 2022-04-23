package com.myplaygroup.app.feature_main.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.myplaygroup.app.feature_main.presentation.MainViewModel

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    viewModel.mainViewModel = mainViewModel
    val imageUri = viewModel.state.imageUri

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
            viewModel.onEvent(HomeScreenEvent.LogoutButtonTapped)
        }) {
            Text(text = "Logout")
        }
    }
}