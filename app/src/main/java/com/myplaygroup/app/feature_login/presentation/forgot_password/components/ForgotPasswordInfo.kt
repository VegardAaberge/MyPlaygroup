package com.myplaygroup.app.feature_login.presentation.forgot_password.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.R

@Composable
fun ForgotPasswordInfo(
    widthModifier: Modifier,
    imageSize: Dp = 200.dp,
    headerFontSize: TextUnit = 30.sp,
) {
    Image(
        painter = painterResource(id = R.drawable.lock_icon),
        contentDescription = null,
        modifier = Modifier
            .size(imageSize)
            .aspectRatio(1f)
            .clip(CircleShape)
    )
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        fontSize = headerFontSize,
        text = stringResource(id = R.string.forgot_password_header),
        modifier = widthModifier,
    )
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = stringResource(id = R.string.forgot_password_explanation),
        modifier = widthModifier
    )
}