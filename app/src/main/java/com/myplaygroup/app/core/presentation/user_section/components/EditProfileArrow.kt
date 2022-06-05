package com.myplaygroup.app.core.presentation.user_section.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun EditProfileArrow(
    editProfileEvent: () -> Unit,
    boxHeight: Dp = 100.dp,
    boxWidth: Dp = 50.dp,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .height(boxHeight)
        .width(boxWidth)
        .clickable {
            editProfileEvent()
        })
    {
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Edit Profile",
            tint = Color.Black,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center)
        )
    }
}