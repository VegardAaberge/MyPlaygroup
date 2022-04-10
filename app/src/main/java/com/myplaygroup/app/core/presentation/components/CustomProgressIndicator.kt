package com.myplaygroup.app.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomProgressIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.3f)
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .align(Alignment.Center)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}