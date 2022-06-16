package com.myplaygroup.app.feature_login.presentation.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme

@Composable
fun LoginImageAndText(
    fontSize: TextUnit = 50.sp,
    imageTextSpace: Dp = 20.dp,
    imageSize: Dp = 200.dp,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.my_playgroup),
        style = MaterialTheme.typography.h3,
        fontFamily = FontFamily(
            Font(R.font.chalkboard, FontWeight.Normal),
        )
    )
    
    Spacer(modifier = Modifier.height(imageTextSpace))

    Image(
        painter = painterResource(id = R.drawable.playgroup),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .padding(horizontal = 50.dp)
            .size(imageSize)
            .aspectRatio(1f)
            .border(
                1.dp,
                Color.LightGray,
                CircleShape
            )
            .padding(1.dp)
            .clip(CircleShape)
    )
}

@Preview(showBackground = true)
@Composable
fun LoginImageAndTextPreview() {
    MyPlaygroupTheme {
        Column {
            LoginImageAndText()
        }
    }
}