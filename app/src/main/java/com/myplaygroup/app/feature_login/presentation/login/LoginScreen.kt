package com.myplaygroup.app.feature_login.presentation.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = viewModel.loginMessage,
            fontSize = 40.sp,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}