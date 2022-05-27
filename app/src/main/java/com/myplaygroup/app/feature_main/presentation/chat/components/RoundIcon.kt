package com.myplaygroup.app.feature_main.presentation.chat.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme

@Composable
fun RowScope.RoundIcon(
    iconUri: Uri?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
){
    Box(
        modifier = modifier
            .align(Alignment.Top),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberImagePainter(data = iconUri),
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .border(
                    1.dp,
                    Color.LightGray,
                    CircleShape
                )
                .clip(CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoundIcon() {
    MyPlaygroupTheme {
        Row {
            RoundIcon(
                iconUri = null,
            )
        }
    }
}